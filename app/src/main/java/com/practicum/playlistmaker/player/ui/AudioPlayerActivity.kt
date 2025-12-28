package com.practicum.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("track", Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("track")
        }
        if (track == null) {
            finish()
            return
        }
        val factory = AudioPlayerViewModel.getFactory(track)
        viewModel = ViewModelProvider(this, factory)[AudioPlayerViewModel::class.java]

        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            playingTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(0)
            durationData.text = track.trackTime
            genreData.text = track.primaryGenreName
            countryData.text = track.country
            buttonPlay.setOnClickListener {
                viewModel.playbackControl()
            }
        }
        Glide.with(binding.albumCover)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(8.dpToPx(binding.albumCover.context)))
            .into(binding.albumCover)

        if (track.collectionName.isEmpty()) {
            binding.albumGroup.isVisible = false
        } else {
            binding.albumData.text = track.collectionName
        }
        if (track.releaseYear.isEmpty()) {
            binding.yearGroup.isVisible = false
        } else {
            binding.yearData.text = track.releaseYear
        }

        viewModel.observePlayerScreenState().observe(this) { state ->
            binding.playingTime.text = state.progressTime
            when (state.playerState) {
                AudioPlayerViewModel.PlayerState.PREPARED -> {
                    binding.buttonPlay.isEnabled = true
                    binding.buttonPlay.setImageResource(R.drawable.button_play)
                }

                AudioPlayerViewModel.PlayerState.PLAYING -> {
                    binding.buttonPlay.setImageResource(R.drawable.button_pause)

                }

                AudioPlayerViewModel.PlayerState.PAUSED -> {
                    binding.buttonPlay.setImageResource(R.drawable.button_play)

                }

                AudioPlayerViewModel.PlayerState.DEFAULT -> {
                    binding.buttonPlay.isEnabled = false
                }
            }
        }

        binding.backArrow.setOnClickListener {
            viewModel.stopPlayer()
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyPlayer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}
