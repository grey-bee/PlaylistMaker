package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop

class AudioPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)

        val track = intent.getSerializableExtra("track", Track::class.java)
        val buttonBack = findViewById<ImageView>(R.id.backArrow)
        val albumCover = findViewById<ImageView>(R.id.albumCover)
        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val buttonAddToPlaylist = findViewById<ImageButton>(R.id.buttonAddToPlaylist) //todo
        val buttonPlay = findViewById<ImageButton>(R.id.buttonPlay) //todo
        val buttonLike = findViewById<ImageButton>(R.id.buttonLike) //todo
        val playingTime = findViewById<TextView>(R.id.playingTime)
        val durationData = findViewById<TextView>(R.id.durationData)
        val albumData = findViewById<TextView>(R.id.albumData)
        val yearData = findViewById<TextView>(R.id.yearData)
        val genreData = findViewById<TextView>(R.id.genreData)
        val countryData = findViewById<TextView>(R.id.countryData)
        val albumGroup = findViewById<Group>(R.id.albumGroup)
        val yearGroup = findViewById<Group>(R.id.yearGroup)

        trackName.text = track?.trackName
        artistName.text = track?.artistName
        playingTime.text = "00:00" //todo
        durationData.text = track?.trackTime


        genreData.text = track?.primaryGenreName
        countryData.text = track?.country
        Glide.with(albumCover)
            .load(track?.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop())
            .into(albumCover)

        if (track?.collectionName.isNullOrEmpty()) {
            albumGroup.isVisible = false
        } else {
            albumData.text = track?.collectionName
        }
        if (track?.releaseDate.isNullOrEmpty()) {
            yearGroup.isVisible = false
        } else {
            yearData.text = track?.year
        }




        buttonBack.setOnClickListener {
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
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}