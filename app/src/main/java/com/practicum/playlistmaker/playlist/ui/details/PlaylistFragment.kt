package com.practicum.playlistmaker.playlist.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.BundleCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.playlist.domain.model.Playlist
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
        ) { "Playlist is required" }
    }
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
        binding.apply {
            backArrowImage.setOnClickListener {
                findNavController().navigateUp()
            }
            Glide.with(binding.albumCoverImage)
                .load(playlist.imagePath)
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop())
                .into(binding.albumCoverImage)
            playlistNameText.text = playlist.name
            playlistDescriptionText.text = playlist.description
            playlistInfoText.text = "${playlist.trackCount} ${R.plurals.tracks_count}"
//            shareButton
//            settingsButton
        }

    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist"
        fun createArgs(playlist: Playlist): Bundle =
            bundleOf(ARGS_PLAYLIST to playlist)
    }


}