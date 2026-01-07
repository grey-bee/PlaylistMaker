package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.getValue

class AudioPlayerFragment : Fragment() {
    private lateinit var binding: FragmentAudioPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = requireNotNull(
            BundleCompat.getParcelable(
                requireArguments(),
                ARGS_TRACK,
                Track::class.java
            )
        ) {
            "Track is required"
        }
        val viewModel: AudioPlayerViewModel by viewModel() {
            parametersOf(track)
        }

        binding.apply {
            trackNameText.text = track.trackName
            artistNameText.text = track.artistName
            playingTimeText.text = SimpleDateFormat("mm:ss", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(0)
            durationText.text = track.trackTime
            genreText.text = track.primaryGenreName
            countryText.text = track.country
            playButton.setOnClickListener {
                viewModel.playbackControl()
            }
        }
        Glide.with(binding.albumCoverImage)
            .load(track.artworkUrl512)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(8.dpToPx(binding.albumCoverImage.context)))
            .into(binding.albumCoverImage)
        if (track.collectionName.isEmpty()) {
            binding.albumGroup.isVisible = false
        } else {
            binding.albumText.text = track.collectionName
        }
        if (track.releaseYear.isEmpty()) {
            binding.yearGroup.isVisible = false
        } else {
            binding.yearText.text = track.releaseYear
        }

        viewModel.observePlayerScreenState().observe(viewLifecycleOwner) { state ->
            binding.playingTimeText.text = state.progressTime
            when (state.playerState) {
                AudioPlayerViewModel.PlayerState.PREPARED -> {
                    binding.playButton.isEnabled = true
                    binding.playButton.setImageResource(R.drawable.button_play)
                }

                AudioPlayerViewModel.PlayerState.PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.button_pause)

                }

                AudioPlayerViewModel.PlayerState.PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.button_play)

                }

                AudioPlayerViewModel.PlayerState.DEFAULT -> {
                    binding.playButton.isEnabled = false
                }
            }
        }

        binding.backArrowImage.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        private const val ARGS_TRACK = "track"
        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)
    }
}