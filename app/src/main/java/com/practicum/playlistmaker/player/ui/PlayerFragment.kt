package com.practicum.playlistmaker.player.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.dpToPx
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import kotlin.getValue
import kotlin.requireNotNull

class PlayerFragment : Fragment() {
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var playlistsAdapter: PlaylistsAdapter
    private lateinit var playlistClickDebounce: (Playlist) -> Unit
    private val track by lazy {
        requireNotNull(
            BundleCompat.getParcelable(
                requireArguments(),
                ARGS_TRACK,
                Track::class.java
            )
        ) { "Track is required" }
    }
    val viewModel: PlayerViewModel by viewModel() {
        parametersOf(track)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeToastMessage().observe(viewLifecycleOwner) { text ->
            Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
        }

        val bottomSheetContainer = binding.standardBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.addToPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.darkScreen.visibility = View.VISIBLE
            binding.darkScreen.alpha = 1f
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.darkScreen.visibility = View.GONE
                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.darkScreen.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset < 0) binding.darkScreen.alpha = (slideOffset + 1) / 2
            }
        })

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
            likeButton.setOnClickListener {
                viewModel.onFavoriteClicked(track)

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
        viewModel.observeIsFavorite().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) binding.likeButton.setImageResource(R.drawable.button_like)
            else binding.likeButton.setImageResource(R.drawable.button_unlike)
        }

        viewModel.observePlayerScreenState().observe(viewLifecycleOwner) { state ->
            binding.playingTimeText.text = state.progress
            when (state) {
                is PlayerState.Prepared -> {
                    binding.playButton.isEnabled = true
                    binding.playButton.setImageResource(R.drawable.button_play)
                }

                is PlayerState.Playing -> {
                    binding.playButton.setImageResource(R.drawable.button_pause)
                }

                is PlayerState.Paused -> {
                    binding.playButton.setImageResource(R.drawable.button_play)
                }

                is PlayerState.Default -> {
                    binding.playButton.isEnabled = false
                }
            }
        }

        playlistClickDebounce =
            debounce<Playlist>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { playlist ->
                if (viewModel.onAddToPlaylistClicked(playlist)) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }

        playlistsAdapter = PlaylistsAdapter(emptyList(), { item -> playlistClickDebounce(item) })
        binding.playlistsRecyclerView.adapter = playlistsAdapter

        viewModel.observePlaylists().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsState.Empty -> {}
                is PlaylistsState.Content -> {
                    playlistsAdapter.updatePlaylists(state.playlists)
                }
            }
        }

        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_newPlaylistFragment)
        }
        setFragmentResultListener("new_playlist") { _, bundle ->
            val title = bundle.getString("title")
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            Toast.makeText(requireContext(), "Плейлист $title создан", Toast.LENGTH_LONG).show()
        }
        binding.backArrowImage.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 500L
        private const val ARGS_TRACK = "track"
        fun createArgs(track: Track): Bundle =
            bundleOf(ARGS_TRACK to track)
    }
}