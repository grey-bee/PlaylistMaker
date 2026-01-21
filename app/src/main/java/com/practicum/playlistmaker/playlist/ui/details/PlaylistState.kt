package com.practicum.playlistmaker.playlist.ui.details

import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track

sealed interface PlaylistState {
    data class Content(
        val playlist: Playlist,
        val playlistTimeSec: Int,
        val playlistTracks: List<Track>
    ) : PlaylistState

    data object Empty : PlaylistState
}