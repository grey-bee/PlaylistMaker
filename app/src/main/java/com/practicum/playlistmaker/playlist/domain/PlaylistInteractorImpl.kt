package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun add(playlist: Playlist) {
        repository.add(playlist)
    }

    override suspend fun del(playlist: Playlist) {
        repository.del(playlist)
    }

    override fun get(): Flow<List<Playlist>> {
        return repository.get()
    }
}