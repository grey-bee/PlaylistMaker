package com.practicum.playlistmaker.favorites.domain

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override suspend fun deleteTrack(trackId: String) {
        repository.deleteTrack(trackId)
    }

    override fun getTracks(): Flow<List<Track>> {
        return repository.getTracks()
    }

    override suspend fun isFavorite(trackId: String): Boolean {
        return repository.isFavorite(trackId)
    }
}