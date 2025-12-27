package com.practicum.playlistmaker.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.model.Track


class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private var tracks = arrayListOf<Track>()
    private var historyTracks = arrayListOf<Track>()
    private var searchRequest = ""
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchRecycleView.adapter = trackAdapter
        binding.recycleViewHistory.adapter = historyAdapter

        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.isNullOrEmpty()) {
                    binding.buttonSearchClear.isVisible = false
                    if (historyTracks.isNotEmpty()) showHistory()
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
            }
        }

        binding.historyClear.setOnClickListener {
//            clearHistory()
        }

        binding.searchField.setOnFocusChangeListener { _, hasFocus ->
//            updateHistory()
            if (hasFocus
                && historyTracks.isNotEmpty()
                && binding.searchField.text.isNullOrEmpty()

            ) closeAll()
            else binding.layoutHistory.isVisible = false
        }

        binding.buttonSearchClear.setOnClickListener {
            binding.searchField.text.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchField.windowToken, 0)
            tracks.clear()
//            updateHistory()
            closeAll()
//            trackAdapter.notifyDataSetChanged()
        }
        binding.refreshButton.setOnClickListener {
            showLoading()
        }
        binding.toolbar.setNavigationOnClickListener {
            finish()
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
//            if (clickDebounce()) {
//                openAudioPlayer(item)
//            }
        }
    )
    private val historyAdapter = TrackAdapter(
        historyTracks,
        onItemClick = { item ->
//            openAudioPlayer(item)
        }
    )

    private fun showLoading() {
        binding.apply {
            searchRecycleView.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = false
            layoutHistory.isVisible = false
            progress.isVisible = true
        }
    }

    private fun showContent(data: List<Track>) {
        tracks.clear()
        tracks.addAll(data)
        trackAdapter.notifyDataSetChanged()

        binding.apply {
            progress.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = false
            searchRecycleView.isVisible = true
        }
    }

    private fun showNothingFound() {
        binding.apply {
            progress.isVisible = false
            searchRecycleView.isVisible = false
            nothingFound.isVisible = true
            noConnection.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            progress.isVisible = false
            searchRecycleView.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = true
        }
    }

    private fun showHistory() {
        binding.apply {
            progress.isVisible = false
            searchRecycleView.isVisible = true
            nothingFound.isVisible = false
            noConnection.isVisible = false
        }
    }

    private fun closeAll() {
        binding.apply {
            progress.isVisible = false
            searchRecycleView.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = false
        }
//        val historySource = getSearchHistoryInteractor()
//        layoutHistory.isVisible = historySource.isNotEmpty()
    }

    //    private fun openAudioPlayer(item: Track) {
//        addTrackToHistoryInteractor(item)
//        val audioPlayerDisplayIntent = Intent(this, AudioPlayerActivity::class.java)
//        audioPlayerDisplayIntent.putExtra("track", item)
//        startActivity(audioPlayerDisplayIntent)
//    }

}