package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun add(playlist: Playlist)
    suspend fun del(playlist: Playlist)
    fun get(): Flow<List<Playlist>>
}