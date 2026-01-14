package com.practicum.playlistmaker.favorites.domain

import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(trackId: String)
    fun getTracks(): Flow<List<Track>>
    suspend fun isFavorite(trackId: String): Boolean
}