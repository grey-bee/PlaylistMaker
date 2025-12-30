package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.AudioPlayerActivity
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()
    private var tracks = arrayListOf<Track>()
    private var historyTracks = arrayListOf<Track>()
    private var searchRequest = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getSearchHistory()
        binding.searchRecycleView.adapter = trackAdapter
        binding.recycleViewHistory.adapter = historyAdapter

        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.isNullOrEmpty()) {
                    binding.buttonSearchClear.isVisible = false
                } else {
                    binding.buttonSearchClear.isVisible = true
                    viewModel.onTextChanged(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }

        binding.searchField.addTextChangedListener(searchWatcher)

        viewModel.observeState().observe(this) { state ->
            when (state) {
                is SearchScreenState.Empty -> showNothingFound()
                is SearchScreenState.Error -> showError()
                is SearchScreenState.Loading -> showLoading()
                is SearchScreenState.Content -> showContent(state.tracks)
                is SearchScreenState.History -> showHistory(state.tracks)
            }
        }

        binding.searchField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus
                && historyTracks.isNotEmpty()
                && binding.searchField.text.isNullOrEmpty()
            ) viewModel.getSearchHistory()
            else closeAll()
        }

        binding.buttonSearchClear.setOnClickListener {
            binding.searchField.text.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchField.windowToken, 0)
            tracks.clear()
            viewModel.getSearchHistory()
        }
        binding.refreshButton.setOnClickListener {
            viewModel.onTextChanged(binding.searchField.text.toString())
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.historyClear.setOnClickListener {
            viewModel.historyClear()
            closeAll()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        handler.removeCallbacksAndMessages(null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_REQUEST", searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString("SEARCH_REQUEST", "")
        binding.searchField.setText(searchRequest)
    }

    private val trackAdapter = TrackAdapter(
        tracks,
        onItemClick = { item ->
            if (viewModel.clickDebounce()) {
                openAudioPlayer(item)
            }
        }
    )
    private val historyAdapter = TrackAdapter(
        historyTracks,
        onItemClick = { item ->
            openAudioPlayer(item)
        }
    )

    private fun showContent(data: List<Track>) {
        tracks.clear()
        tracks.addAll(data)
        trackAdapter.notifyDataSetChanged()

        binding.apply {
            searchRecycleView.isVisible = true
            historyLayout.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = false
            loading.isVisible = false
        }
    }

    private fun showHistory(data: List<Track>) {
        historyTracks.clear()
        historyTracks.addAll(data)
        historyAdapter.notifyDataSetChanged()
        binding.apply {
            searchRecycleView.isVisible = false
            if (historyTracks.isNotEmpty()) historyLayout.isVisible = true
            nothingFound.isVisible = false
            noConnection.isVisible = false
            loading.isVisible = false
        }
    }

    private fun showLoading() {
        binding.apply {
            searchRecycleView.isVisible = false
            historyLayout.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = false
            loading.isVisible = true
        }
    }

    private fun showNothingFound() {
        binding.apply {
            searchRecycleView.isVisible = false
            historyLayout.isVisible = false
            nothingFound.isVisible = true
            noConnection.isVisible = false
            loading.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            searchRecycleView.isVisible = false
            historyLayout.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = true
            loading.isVisible = false
        }
    }

    private fun closeAll() {
        binding.apply {
            searchRecycleView.isVisible = false
            historyLayout.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = false
            loading.isVisible = false
        }
    }

    private fun openAudioPlayer(item: Track) {
        viewModel.addTrackToHistory(item)
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra("track", item)
        startActivity(intent)
    }
}