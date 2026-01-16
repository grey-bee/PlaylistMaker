package com.practicum.playlistmaker.playlist.data

import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.playlist.data.convertors.PlaylistDbConvertor
import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: PlaylistDbConvertor
) :
    PlaylistRepository {
    override suspend fun add(playlist: Playlist) {
        appDatabase.playlistDao().add(convertPlaylistToEntity(playlist))
    }

    override suspend fun del(playlist: Playlist) {
        appDatabase.playlistDao().delete(convertPlaylistToEntity(playlist))
    }

    override fun get(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().get()
            .map { entities -> entities.map { entity -> convertEntityToPlaylist(entity) } }
    }

    private fun convertEntityToPlaylist(entity: PlaylistEntity): Playlist {
        return convertor.map(entity)
    }

    private fun convertPlaylistToEntity(playlist: Playlist): PlaylistEntity {
        return convertor.map(playlist)
    }
}