package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
        val btnMedia = findViewById<Button>(R.id.button_media)
        val btnSettings = findViewById<Button>(R.id.button_settings)

        val SearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Нажали на Поиск!", Toast.LENGTH_SHORT).show()
            }
        }

        btnSearch.setOnClickListener(SearchClickListener)

        btnMedia.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на Медиатеку!", Toast.LENGTH_SHORT).show()
        }

        btnSettings.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажали на Настройки!", Toast.LENGTH_SHORT).show()
        }


    }
}