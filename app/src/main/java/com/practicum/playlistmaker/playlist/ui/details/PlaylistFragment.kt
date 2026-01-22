package com.practicum.playlistmaker.playlist.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.getValue

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistBinding
    private val playlist by lazy {
        requireNotNull(
            BundleCompat.getParcelable(
                requireArguments(),
                ARGS_PLAYLIST,
                Playlist::class.java
            )
        ) { R.string.playlist_is_required }
    }
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var trackClickDebounce: (Track) -> Unit
    private val playlistViewModel: PlaylistViewModel by viewModel() {
        parametersOf(playlist)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backArrowImage.setOnClickListener {
            findNavController().navigateUp()
        }
        trackClickDebounce =
            debounce<Track>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { track ->
                openAudioPlayer(track)
            }
        trackAdapter = TrackAdapter(
            emptyList(),
            { item -> trackClickDebounce(item) },
            { item ->
                MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.do_you_want_to_detele_track)
                    .setNegativeButton(R.string.no) { _, _ -> }
                    .setPositiveButton(R.string.yes) { _, _ -> playlistViewModel.deleteTrack(item) }
                    .show()
            })
        binding.playlistRecyclerView.adapter = trackAdapter

        playlistViewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistState.Content -> {
                    binding.apply {
                        Glide.with(albumCoverImage)
                            .load(state.playlist.imagePath)
                            .placeholder(R.drawable.placeholder)
                            .transform(CenterCrop())
                            .into(albumCoverImage)
                        Glide.with(albumCoverImage)
                            .load(state.playlist.imagePath)
                            .placeholder(R.drawable.placeholder)
                            .transform(CenterCrop())
                            .into(settingsAlbumCover)
                        playlistNameText.text = state.playlist.name
                        settingsPlaylistName.text = state.playlist.name
                        playlistDescriptionText.text = state.playlist.description
                        settingsTrackCounter.text = resources.getQuantityString(
                            R.plurals.tracks_count,
                            state.playlist.trackCount,
                            state.playlist.trackCount
                        )
                        playlistInfoText.text = getString(
                            R.string.playlist_info,
                            resources.getQuantityString(
                                R.plurals.tracks_time,
                                state.playlistTimeSec,
                                state.playlistTimeSec
                            ),
                            resources.getQuantityString(
                                R.plurals.tracks_count,
                                state.playlist.trackCount,
                                state.playlist.trackCount
                            )
                        )

                    }

                    showContent(state.playlistTracks)
                }

                is PlaylistState.Empty -> {}
            }
        }
        binding.shareButton.setOnClickListener {
            if (playlist.trackIds.isEmpty()) {
                Toast.makeText(
                    requireContext(), R.string.no_tracks_for_share, Toast.LENGTH_LONG
                ).show()
            } else {
                val data = playlistViewModel.playlistShare()
                val intent = Intent(Intent.ACTION_SEND)
                val chooserTitle = data.title
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, data.text)
                startActivity(Intent.createChooser(intent, chooserTitle))
            }
        }

        binding.root.doOnLayout {
            val anchorElement = binding.shareButton
            val screenHeight = binding.root.height
            val peekHeight = screenHeight - anchorElement.bottom - 24
            val behavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
            behavior.peekHeight = peekHeight
        }

        val bottomSheetContainer = binding.settingsBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.settingsButton.setOnClickListener {
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


    }


    private fun showContent(data: List<Track>) {
        trackAdapter.updateTracks(data)
        trackAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(ARGS_PLAYLIST to playlist)
    }

    private fun openAudioPlayer(item: Track) {
        val bundle = PlayerFragment.createArgs(item)
        findNavController().navigate(R.id.action_playlistFragment_to_audioPlayerFragment, bundle)
    }
}