package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
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
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getSearchHistory()
        binding.searchRecyclerView.adapter = trackAdapter
        binding.historyRecyclerView.adapter = historyAdapter

        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.isNullOrEmpty()) {
                    binding.searchClearButton.isVisible = false
                } else {
                    binding.searchClearButton.isVisible = true
                    viewModel.onTextChanged(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }

        binding.searchInput.addTextChangedListener(searchWatcher)

        viewModel.observeState().observe(this) { state ->
            when (state) {
                is SearchScreenState.Empty -> showNothingFound()
                is SearchScreenState.Error -> showError()
                is SearchScreenState.Loading -> showLoading()
                is SearchScreenState.Content -> showContent(state.tracks)
                is SearchScreenState.History -> showHistory(state.tracks)
            }
        }

        binding.searchInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus
                && historyTracks.isNotEmpty()
                && binding.searchInput.text.isNullOrEmpty()
            ) viewModel.getSearchHistory()
            else closeAll()
        }

        binding.searchClearButton.setOnClickListener {
            binding.searchInput.text.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchInput.windowToken, 0)
            tracks.clear()
            viewModel.getSearchHistory()
        }
        binding.refreshButton.setOnClickListener {
            viewModel.onTextChanged(binding.searchInput.text.toString())
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.historyClearButton.setOnClickListener {
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
        binding.searchInput.setText(searchRequest)
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
            searchRecyclerView.isVisible = true
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showHistory(data: List<Track>) {
        historyTracks.clear()
        historyTracks.addAll(data)
        historyAdapter.notifyDataSetChanged()
        binding.apply {
            searchRecyclerView.isVisible = false
            if (historyTracks.isNotEmpty()) historyLayout.isVisible = true
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showLoading() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = true
        }
    }

    private fun showNothingFound() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = true
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun closeAll() {
        binding.apply {
            searchRecyclerView.isVisible = false
            historyLayout.isVisible = false
            nothingFoundPlaceholder.isVisible = false
            noConnectionPlaceholder.isVisible = false
            progressBar.isVisible = false
        }
    }

    private fun openAudioPlayer(item: Track) {
        viewModel.addTrackToHistory(item)
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra("track", item)
        startActivity(intent)
    }
}