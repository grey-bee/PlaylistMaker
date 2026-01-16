package com.practicum.playlistmaker.playlist.domian

import com.practicum.playlistmaker.playlist.domian.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun add(playlist: Playlist)
    suspend fun del(playlist: Playlist)
    fun get(): Flow<List<Playlist>>
}