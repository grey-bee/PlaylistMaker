package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener{
            val mainScreenIntent = Intent(this, MainActivity::class.java)
            startActivity(mainScreenIntent)
        }
    }
}