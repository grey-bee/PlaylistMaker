package com.practicum.playlistmaker.playlist.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.ui.playlist.PlaylistEditScreen
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class NewPlaylistFragment : Fragment() {
    private val playlist: Playlist? by lazy {
        arguments?.let {
            BundleCompat.getParcelable(
                it,
                ARGS_PLAYLIST,
                Playlist::class.java
            )
        }
    }
    val viewModel: NewPlaylistViewModel by viewModel() {
        parametersOf(playlist)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            val nameOfScreen = if (playlist == null) R.string.new_playlist else R.string.edit
            val nameOfButton = if (playlist == null) R.string.create else R.string.save
            setContent {
                PlaylistEditScreen(
                    nameOfScreen = nameOfScreen,
                    nameOfButton = nameOfButton,
                    onPushButton = { viewModel.savePlaylist(it) },
                    playlist = playlist,
                    onPushBack = { findNavController().navigateUp() }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observePlaylistSaved().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { check ->
                if (check) findNavController().navigateUp()
            }
        }
    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(ARGS_PLAYLIST to playlist)
    }
}