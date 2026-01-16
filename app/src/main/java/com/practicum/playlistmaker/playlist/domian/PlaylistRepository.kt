package com.practicum.playlistmaker.playlist.domian

import com.practicum.playlistmaker.playlist.domian.model.Playlist

class PlaylistRepository {
    suspend fun add(playlist: Playlist)
}