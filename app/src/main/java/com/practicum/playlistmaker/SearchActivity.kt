package com.practicum.playlistmaker

import android.os.Bundle
import android.text.TextWatcher
import android.text.Editable
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    var searchRequest = ""
    lateinit var searchField: EditText //вынужденная мера так как в задании нужно восстанавливать в onRestoreInstanceState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        searchField = findViewById(R.id.search_field)
        val clearButton = findViewById<ImageView>(R.id.button_clear)


        clearButton.isVisible = false
        clearButton.setOnClickListener {
            searchField.text.clear()
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchField.windowToken, 0)
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