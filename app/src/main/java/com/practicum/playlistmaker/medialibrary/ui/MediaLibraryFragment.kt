package com.practicum.playlistmaker.medialibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favorites.ui.FavoritesState
import com.practicum.playlistmaker.favorites.ui.FavoritesViewModel
import com.practicum.playlistmaker.player.ui.PlayerFragment
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.details.PlaylistFragment
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsState
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.ui.medialibrary.MediaLibraryScreen
import com.practicum.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaLibraryFragment : Fragment() {
    private val favoritesViewModel: FavoritesViewModel by viewModel()
    private val playlistsViewModel: PlaylistsViewModel by viewModel()
    private lateinit var trackClickDebounce: (Track) -> Unit
    private lateinit var playlistClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
        playlistClickDebounce =
            debounce<Playlist>(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false
            ) { playlist ->
                val bundle = PlaylistFragment.createArgs(playlist)
                findNavController().navigate(
                    R.id.action_mediaLibraryFragment_to_playlistFragment,
                    bundle
                )
            }
        return ComposeView(requireContext()).apply {
            setContent {
                val favoritesState by favoritesViewModel.observeState().observeAsState(
                    FavoritesState.Empty
                )
                val playlistsState by playlistsViewModel.observeState().observeAsState(
                    PlaylistsState.Empty
                )
                MediaLibraryScreen(
                    favoritesState = favoritesState,
                    playlistsState = playlistsState,
                    onNewPlaylist = { findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment) },
                    onTrackClick = { trackClickDebounce(it) },
                    onPlaylistClick = { playlistClickDebounce(it) }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setFragmentResultListener("new_playlist") { _, bundle ->
            val title = bundle.getString("title")
            Toast.makeText(requireContext(), "Плейлист $title создан", Toast.LENGTH_LONG).show()
        }
    }

    private fun openAudioPlayer(track: Track) {
        val bundle = PlayerFragment.createArgs(track)
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_audioPlayerFragment,
            bundle
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}