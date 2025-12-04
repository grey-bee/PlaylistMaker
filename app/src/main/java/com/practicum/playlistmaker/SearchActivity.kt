package com.practicum.playlistmaker

import android.os.Bundle
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

class SearchActivity : AppCompatActivity() {

    private var tracks = arrayListOf<Track>()

    var searchRequest = ""
    lateinit var searchField: EditText //вынужденная мера так как в задании нужно восстанавливать в onRestoreInstanceState


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        searchField = findViewById(R.id.search_field)
        val clearButton = findViewById<ImageView>(R.id.button_clear)
        val refreshButton = findViewById<Button>(R.id.refresh_button)
        val nothingFoundLayer = findViewById<LinearLayout>(R.id.nothing_found)
        val noConnectionLayer = findViewById<LinearLayout>(R.id.no_connection)
        val recyclerView = findViewById<RecyclerView>(R.id.recycle_view)
        val trackAdapter = TrackAdapter(tracks)
        recyclerView.isVisible = true
        recyclerView.adapter = trackAdapter

        fun showContent() {
            recyclerView.isVisible = true
            nothingFoundLayer.isVisible = false
            noConnectionLayer.isVisible = false
        }

        fun showNothingFound() {
            recyclerView.isVisible = false
            nothingFoundLayer.isVisible = true
            noConnectionLayer.isVisible = false
        }

        fun showError() {
            recyclerView.isVisible = false
            nothingFoundLayer.isVisible = false
            noConnectionLayer.isVisible = true
        }

        fun closeAll() {
            recyclerView.isVisible = false
            nothingFoundLayer.isVisible = false
            noConnectionLayer.isVisible = false
        }

        fun performSearch() {
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
                                showContent()
                                tracks.addAll(resultList)
                                trackAdapter.notifyDataSetChanged()
                            } else {
                                showNothingFound()
                            }
                        } else {
                            showError()
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showError()
                    }
                })

        }

        clearButton.setOnClickListener {
            searchField.text.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
            tracks.clear()
            closeAll()
            trackAdapter.notifyDataSetChanged()

        }

        refreshButton.setOnClickListener {
            performSearch()
        }

        val searchWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearButton.isVisible = false
                    return
                } else {
                    clearButton.isVisible = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                searchRequest = s.toString()
            }
        }

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                performSearch()
            }
            false
        }

        searchField.addTextChangedListener(searchWatcher)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
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
