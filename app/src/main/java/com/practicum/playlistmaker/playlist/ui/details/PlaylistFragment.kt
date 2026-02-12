package com.practicum.playlistmaker.playlist.ui.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.create.NewPlaylistFragment
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.playlist.PlaylistScreen
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {
    private val playlist by lazy {
        requireNotNull(
            BundleCompat.getParcelable(
                requireArguments(),
                ARGS_PLAYLIST,
                Playlist::class.java
            )
        ) { R.string.playlist_is_required }
    }
    private lateinit var trackClickDebounce: (Track) -> Unit
    private val playlistViewModel: PlaylistViewModel by viewModel() {
        parametersOf(playlist)
    }

    override fun onResume() {
        super.onResume()
        playlistViewModel.loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        trackClickDebounce =
            debounce<Track>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { track ->
                openAudioPlayer(track)
            }
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val playlistState by playlistViewModel.observeState()
                    .observeAsState(PlaylistState.Empty)
                PlaylistScreen(
                    onShareClick = { sharePlaylist() },
                    onInfoEditingClick = {
                        val bundle = NewPlaylistFragment.createArgs(playlist)
                        findNavController().navigate(
                            R.id.action_playlistFragment_to_newPlaylistFragment, bundle
                        )
                    },
                    onPlaylistDeleteClick = {
                        playlistViewModel.deletePlaylist(playlist)
                        findNavController().navigateUp()
                    },
                    onTrackClick = { trackClickDebounce(it) },
                    playlistState = playlistState,
                    onPushBack = { findNavController().navigateUp() }
                )
            }
        }
    }

    fun sharePlaylist() {
        if (playlist.trackIds.isEmpty()) {
            Toast.makeText(
                requireContext(), R.string.no_tracks_for_share, Toast.LENGTH_LONG
            ).show()
        } else {
            val text = playlistViewModel.getShareText()
            val intent = Intent(Intent.ACTION_SEND)
            val chooserTitle = ""
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(intent, chooserTitle))
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistViewModel.loadData()
    }


    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(ARGS_PLAYLIST to playlist)
    }

    private fun openAudioPlayer(item: Track) {
        val bundle = PlayerFragment.createArgs(item)
        findNavController().navigate(
            R.id.action_playlistFragment_to_audioPlayerFragment,
            bundle
        )
    }
}