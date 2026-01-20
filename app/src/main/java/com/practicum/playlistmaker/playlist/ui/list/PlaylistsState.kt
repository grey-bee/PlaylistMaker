package com.practicum.playlistmaker.playlist.ui.list

import com.practicum.playlistmaker.playlist.domain.model.Playlist

sealed interface PlaylistsState {
    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

    data object Empty : PlaylistsState
}