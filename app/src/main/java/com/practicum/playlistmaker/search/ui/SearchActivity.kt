package com.practicum.playlistmaker.search.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.model.Track

//import com.practicum.playlistmaker.domain.api.SearchTracksInteractor
//import com.practicum.playlistmaker.domain.models.Track
//import com.practicum.playlistmaker.old.creator.ui.audioplayer.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var tracks = arrayListOf<Track>()
    private var historyTracks = arrayListOf<Track>()
    private var searchRequest = ""
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

//    private val addTrackToHistoryInteractor by lazy {
//        Creator.provideAddTrackToHistoryInteractor(
//            this
//        )
//    }
//    private val getSearchHistoryInteractor by lazy { Creator.provideGetSearchHistoryInteractor(this) }
//    private val clearSearchHistoryInteractor by lazy {
//        Creator.provideClearSearchHistoryInteractor(
//            this
//        )
//    }
//    private val searchTracksInteractor by lazy { Creator.provideSearchTracksInteractor() }

//    private fun openAudioPlayer(item: Track) {
//        addTrackToHistoryInteractor(item)
//        val audioPlayerDisplayIntent = Intent(this, AudioPlayerActivity::class.java)
//        audioPlayerDisplayIntent.putExtra("track", item)
//        startActivity(audioPlayerDisplayIntent)
//    }

//    private val trackAdapter = TrackAdapter(
//        tracks,
//        onItemClick = { item ->
//            if (clickDebounce()) {
//                openAudioPlayer(item)
//            }
//        }
//    )

    private fun showContent() {
        binding.apply {
            recycleView.isVisible = true
            nothingFound.isVisible = false
            noConnection.isVisible = false
        }
    }

    private fun showNothingFound() {
        binding.apply {
            recycleView.isVisible = false
            nothingFound.isVisible = true
            noConnection.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            recycleView.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = true
        }
    }

    private fun closeAll() {
        binding.apply {
            recycleView.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = false
        }
//        val historySource = getSearchHistoryInteractor()
//        layoutHistory.isVisible = historySource.isNotEmpty()
    }

    private fun performSearch() {
        binding.apply {
            recycleView.isVisible = false
            nothingFound.isVisible = false
            noConnection.isVisible = false
            layoutHistory.isVisible = false
            progress.isVisible = true
        }

//        searchTracksInteractor(
//            searchField.text.toString(),
//            object : SearchTracksInteractor.Consumer {
//                override fun consume(foundTracks: List<Track>?) {
//                    tracks.clear()
//                    progress.isVisible = false
//                    if (foundTracks != null) {
//                        if (foundTracks.isNotEmpty()) {
//                            showContent()
//                            tracks.addAll(foundTracks)
//                            trackAdapter.notifyDataSetChanged()
//                        } else {
//                            showNothingFound()
//                        }
//                    } else {
//                        showError()
//                    }
//                }
//            }
//        )
    }

    private val searchRunnable = Runnable { performSearch() }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

//    private val historyAdapter = TrackAdapter(
//        historyTracks,
//        onItemClick = { item ->
//            openAudioPlayer(item)
//        }
//    )

    private fun updateHistory() {
//        val historySource = getSearchHistoryInteractor()
        historyTracks.clear()
//        if (historySource.isNotEmpty()) historyTracks.addAll(historySource)
//        historyAdapter.notifyDataSetChanged()
    }

    private fun clearHistory() {
//        clearSearchHistoryInteractor()
        historyTracks.clear()
//        historyAdapter.notifyDataSetChanged()
        binding.layoutHistory.isVisible = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycleView.isVisible = true
//        recyclerView.adapter = trackAdapter

//        historyRecyclerView.adapter = historyAdapter

        binding.historyClear.setOnClickListener {
            clearHistory()
        }

        binding.searchField.setOnFocusChangeListener { _, hasFocus ->
            updateHistory()
            if (hasFocus
                && historyTracks.isNotEmpty()
                && binding.searchField.text.isNullOrEmpty()

            ) closeAll()
            else binding.layoutHistory.isVisible = false
        }

        binding.buttonClear.setOnClickListener {
            binding.searchField.text.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchField.windowToken, 0)
            tracks.clear()
            updateHistory()
            closeAll()
//            trackAdapter.notifyDataSetChanged()

        }

        binding.refreshButton.setOnClickListener {
            performSearch()
        }

        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.isNullOrEmpty()) {
                    binding.buttonClear.isVisible = false
                    if (historyTracks.isNotEmpty()) binding.layoutHistory.isVisible = true
                } else {
                    binding.buttonClear.isVisible = true
                    searchDebounce()
                    binding.layoutHistory.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }

        binding.searchField.addTextChangedListener(searchWatcher)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("SEARCH_REQUEST", searchRequest)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchRequest = savedInstanceState.getString("SEARCH_REQUEST", "")
        binding.searchField.setText(searchRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}