package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<TextView>(R.id.button_back)

        backButton.setOnClickListener {
            val mainScreenIntent = Intent(this, MainActivity::class.java)
            startActivity(mainScreenIntent)
        }
    }
}