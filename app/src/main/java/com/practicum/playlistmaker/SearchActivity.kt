package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextWatcher
import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var tracks = arrayListOf<Track>()
    private var historyTracks = arrayListOf<Track>()
    private var searchRequest = ""
    lateinit var searchField: EditText
    lateinit var recyclerView: RecyclerView
    lateinit var nothingFoundLayer: LinearLayout
    lateinit var noConnectionLayer: LinearLayout
    lateinit var layoutHistory: LinearLayout
    lateinit var progress: ProgressBar
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


    private val trackAdapter = TrackAdapter(
        tracks,
        onItemClick = { item ->
            if (clickDebounce()) {
                SearchHistoryManager.addTrack(item)
                val audioPlayerDisplayIntent = Intent(this, AudioPlayerActivity::class.java)
                audioPlayerDisplayIntent.putExtra("track", item)
                startActivity(audioPlayerDisplayIntent)
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
        val historySource = SearchHistoryManager.getTrackList()
        layoutHistory.isVisible = historySource.isNotEmpty()
    }

    private fun performSearch() {
        recyclerView.isVisible = false
        nothingFoundLayer.isVisible = false
        noConnectionLayer.isVisible = false
        layoutHistory.isVisible = false
        progress.isVisible = true
        RetrofitClient.api.search(searchField.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.isSuccessful) {
                        val resultList = response.body()?.results
                        tracks.clear()
                        if (!resultList.isNullOrEmpty()) {
                            progress.isVisible = false
                            showContent()
                            tracks.addAll(resultList)
                            trackAdapter.notifyDataSetChanged()
                        } else {
                            progress.isVisible = false
                            showNothingFound()
                        }
                    } else {
                        progress.isVisible = false
                        showError()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    progress.isVisible = false
                    showError()
                }
            })

    }

    private val searchRunnable = Runnable { performSearch() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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


        val historyAdapter = TrackAdapter(
            historyTracks,
            onItemClick = { item ->
                val audioPlayerDisplayIntent = Intent(this, AudioPlayerActivity::class.java)
                audioPlayerDisplayIntent.putExtra("track", item)
                startActivity(audioPlayerDisplayIntent)
            }
        )
        recyclerView.isVisible = true
        recyclerView.adapter = trackAdapter

        historyRecyclerView.adapter = historyAdapter

        fun updateHistory() {
            val historySource = SearchHistoryManager.getTrackList()
            historyTracks.clear()
            if (historySource.isNotEmpty()) historyTracks.addAll(historySource)
            historyAdapter.notifyDataSetChanged()
        }

        fun clearHistory() {
            SearchHistoryManager.clear()
            historyTracks.clear()
            historyAdapter.notifyDataSetChanged()
            layoutHistory.isVisible = false
        }

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

}
