package com.practicum.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.TrackAdapter
import com.practicum.playlistmaker.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.audioplayer.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {
    private var tracks = arrayListOf<Track>()
    private var historyTracks = arrayListOf<Track>()
    private var searchRequest = ""
    private lateinit var searchField: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var nothingFoundLayer: LinearLayout
    private lateinit var noConnectionLayer: LinearLayout
    private lateinit var layoutHistory: LinearLayout
    private lateinit var progress: ProgressBar
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

    private val addTrackToHistoryInteractor by lazy {
        Creator.provideAddTrackToHistoryInteractor(
            this
        )
    }
    private val getSearchHistoryInteractor by lazy { Creator.provideGetSearchHistoryInteractor(this) }
    private val clearSearchHistoryInteractor by lazy {
        Creator.provideClearSearchHistoryInteractor(
            this
        )
    }
    private val searchTracksInteractor by lazy { Creator.provideSearchTracksInteractor() }

    private fun openAudioPlayer(item: Track) {
        addTrackToHistoryInteractor.execute(item)
        val audioPlayerDisplayIntent = Intent(this, AudioPlayerActivity::class.java)
        audioPlayerDisplayIntent.putExtra("track", item)
        startActivity(audioPlayerDisplayIntent)
    }

    private val trackAdapter = TrackAdapter(
        tracks,
        onItemClick = { item ->
            if (clickDebounce()) {
                openAudioPlayer(item)
            }
        }
    )

    private fun showContent() {
        recyclerView.isVisible = true
        nothingFoundLayer.isVisible = false
        noConnectionLayer.isVisible = false
    }

    private fun showNothingFound() {
        recyclerView.isVisible = false
        nothingFoundLayer.isVisible = true
        noConnectionLayer.isVisible = false
    }

    private fun showError() {
        recyclerView.isVisible = false
        nothingFoundLayer.isVisible = false
        noConnectionLayer.isVisible = true
    }

    private fun closeAll() {
        recyclerView.isVisible = false
        nothingFoundLayer.isVisible = false
        noConnectionLayer.isVisible = false
        val historySource = getSearchHistoryInteractor.execute()
        layoutHistory.isVisible = historySource.isNotEmpty()
    }

    private fun performSearch() {
        recyclerView.isVisible = false
        nothingFoundLayer.isVisible = false
        noConnectionLayer.isVisible = false
        layoutHistory.isVisible = false
        progress.isVisible = true
        Log.d("debugA", "Start searchTracksInteractor.execute")
        Log.d("debugA", "Start searchTracksInteractor.execute")
        Log.d("debugA", searchField.text.toString())
        searchTracksInteractor.execute(
            searchField.text.toString(),
            object : SearchTracksInteractor.Consumer {
                override fun consume(foundTracks: List<Track>?) {
                    tracks.clear()
                    progress.isVisible = false
                    Log.d("debugA", foundTracks.toString())
                    if (foundTracks != null) {
                        if (foundTracks.isNotEmpty()) {
                            showContent()
                            tracks.addAll(foundTracks)
                            trackAdapter.notifyDataSetChanged()
                        } else {
                            showNothingFound()
                        }
                    } else {
                        showError()
                    }
                }
            }
        )
    }

    private val searchRunnable = Runnable { performSearch() }
    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private val historyAdapter = TrackAdapter(
        historyTracks,
        onItemClick = { item ->
            openAudioPlayer(item)
        }
    )

    private fun updateHistory() {
        val historySource = getSearchHistoryInteractor.execute()
        historyTracks.clear()
        if (historySource.isNotEmpty()) historyTracks.addAll(historySource)
        historyAdapter.notifyDataSetChanged()
    }

    private fun clearHistory() {
        clearSearchHistoryInteractor.execute()
        historyTracks.clear()
        historyAdapter.notifyDataSetChanged()
        layoutHistory.isVisible = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        searchField = findViewById(R.id.search_field)
        recyclerView = findViewById(R.id.recycle_view)
        nothingFoundLayer = findViewById(R.id.nothing_found)
        noConnectionLayer = findViewById(R.id.no_connection)
        progress = findViewById(R.id.progress)
        layoutHistory = findViewById(R.id.layout_history)
        val historyRecyclerView = findViewById<RecyclerView>(R.id.recycle_view_history)
        val clearButton = findViewById<ImageView>(R.id.button_clear)
        val historyClearButton = findViewById<Button>(R.id.history_clear)
        val refreshButton = findViewById<Button>(R.id.refresh_button)

        recyclerView.isVisible = true
        recyclerView.adapter = trackAdapter

        historyRecyclerView.adapter = historyAdapter

        historyClearButton.setOnClickListener {
            clearHistory()
        }

        searchField.setOnFocusChangeListener { _, hasFocus ->
            updateHistory()
            if (hasFocus
                && historyTracks.isNotEmpty()
                && searchField.text.isNullOrEmpty()

            ) closeAll()
            else layoutHistory.isVisible = false
        }

        clearButton.setOnClickListener {
            searchField.text.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
            tracks.clear()
            updateHistory()
            closeAll()
            trackAdapter.notifyDataSetChanged()

        }

        refreshButton.setOnClickListener {
            performSearch()
        }

        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.isNullOrEmpty()) {
                    clearButton.isVisible = false
                    if (historyTracks.isNotEmpty()) layoutHistory.isVisible = true
                } else {
                    clearButton.isVisible = true
                    searchDebounce()
                    layoutHistory.isVisible = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }

        searchField.addTextChangedListener(searchWatcher)

        toolbar.setNavigationOnClickListener {
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
        searchField.setText(searchRequest)
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