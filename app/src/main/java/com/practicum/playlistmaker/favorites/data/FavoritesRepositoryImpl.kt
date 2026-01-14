package com.practicum.playlistmaker.favorites.data

import com.practicum.playlistmaker.favorites.data.convertors.TrackDbConvertor
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.favorites.data.db.TrackEntity
import com.practicum.playlistmaker.favorites.domain.FavoritesRepository
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) :
    FavoritesRepository {
    override suspend fun addTrack(track: Track) {
        appDatabase.trackDao().addTrack(convertTrackToTrackEntity(track))
    }

    override suspend fun deleteTrack(trackId: String) {
        appDatabase.trackDao().deleteTrack(trackId)
    }

    override fun getTracks(): Flow<List<Track>> {
        return appDatabase.trackDao().getTracks()
            .map { entities -> convertTrackEntitiesToTracks(entities) }
    }

    override suspend fun isFavorite(trackId: String): Boolean {
        return appDatabase.trackDao().isFavorite(trackId)
    }

    private fun convertTrackEntitiesToTracks(entities: List<TrackEntity>): List<Track> {
        return entities.map { entity -> trackDbConvertor.map(entity) }
    }

    private fun convertTrackToTrackEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }
}