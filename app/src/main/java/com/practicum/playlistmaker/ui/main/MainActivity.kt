package com.practicum.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.practicum.playlistmaker.ui.medialibrary.MediaLibraryActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.search.SearchActivity
import com.practicum.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSearch = findViewById<Button>(R.id.button_search)
        val btnMediaLibrary = findViewById<Button>(R.id.button_media)
        val btnSettings = findViewById<Button>(R.id.button_settings)

        btnSearch.setOnClickListener {
            val searchDisplayIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchDisplayIntent)
        }

        btnMediaLibrary.setOnClickListener {
            val mediaLibraryDisplayIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(mediaLibraryDisplayIntent)
        }

        btnSettings.setOnClickListener {
            val settingsDisplayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsDisplayIntent)
        }


    }
}