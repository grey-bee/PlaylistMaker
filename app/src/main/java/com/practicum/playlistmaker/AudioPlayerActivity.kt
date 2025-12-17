package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val DELAY = 500L
    }

    private var playerState = STATE_DEFAULT
    private val mediaPlayer = MediaPlayer()
    private lateinit var buttonPlay: ImageButton
    private lateinit var url: String
    private lateinit var playingTime: TextView
    private val handler = Handler(Looper.getMainLooper())

    private fun preparePlayer() {
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.e("AudioPlayer", "MediaPlayer error: what=$what, extra=$extra")
            true
        }
        Log.d("AudioPlayer", "preparePlayer started, url=$url")
        mediaPlayer.setDataSource(url)
        Log.d("AudioPlayer", "setDataSource done")
        mediaPlayer.prepareAsync()
        Log.d("AudioPlayer", "prepareAsync called")
        mediaPlayer.setOnPreparedListener {
            buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            buttonPlay.setImageResource(R.drawable.button_play)
            handler.removeCallbacksAndMessages(null)
            playingTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(0)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        startTimer()
        buttonPlay.setImageResource(R.drawable.button_pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacksAndMessages(null)
        buttonPlay.setImageResource(R.drawable.button_play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startTimer() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val data = mediaPlayer.getCurrentPosition()
                val seconds = data / 1000
                playingTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(data)
                handler.postDelayed(this, DELAY)
            }
        }, DELAY)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        val track = intent.getParcelableExtra("track", Track::class.java)
        val buttonBack = findViewById<ImageView>(R.id.backArrow)
        val albumCover = findViewById<ImageView>(R.id.albumCover)
        val trackName = findViewById<TextView>(R.id.trackName)
        val artistName = findViewById<TextView>(R.id.artistName)
        val buttonAddToPlaylist = findViewById<ImageButton>(R.id.buttonAddToPlaylist) //todo
        buttonPlay = findViewById(R.id.buttonPlay)
        buttonPlay.isEnabled = false
        val buttonLike = findViewById<ImageButton>(R.id.buttonLike) //todo
        playingTime = findViewById(R.id.playingTime)
        val durationData = findViewById<TextView>(R.id.durationData)
        val albumData = findViewById<TextView>(R.id.albumData)
        val yearData = findViewById<TextView>(R.id.yearData)
        val genreData = findViewById<TextView>(R.id.genreData)
        val countryData = findViewById<TextView>(R.id.countryData)
        val albumGroup = findViewById<Group>(R.id.albumGroup)
        val yearGroup = findViewById<Group>(R.id.yearGroup)

        trackName.text = track?.trackName
        artistName.text = track?.artistName
        playingTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(0)
        durationData.text = track?.trackTime
        val test = track?.previewUrl.toString()
        Log.d("debug", test)
        if (!track?.previewUrl.isNullOrEmpty()) {
            url = track?.previewUrl.toString()
            preparePlayer()
        }
        buttonPlay.setOnClickListener {
            playbackControl()
        }

        genreData.text = track?.primaryGenreName
        countryData.text = track?.country
        Glide.with(albumCover)
            .load(track?.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(8.dpToPx(albumCover.context)))
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
            mediaPlayer.stop()
            handler.removeCallbacksAndMessages(null)
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

    override fun onPause() {
        super.onPause()
        pausePlayer()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}