package com.practicum.playlistmaker.playlist.ui

import com.practicum.playlistmaker.playlist.domain.model.Playlist

sealed interface PlaylistState {
    data class Content(
        val playlist: List<Playlist>
    ) : PlaylistState

    data object Empty : PlaylistState
}