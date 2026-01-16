package com.practicum.playlistmaker.playlist.ui

import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.playlist.domain.model.Playlist

class PlaylistFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding
    private lateinit var playlistClickDebounce: (Playlist) -> Unit

}