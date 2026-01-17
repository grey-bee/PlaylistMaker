package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylist()
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): String {
        return repository.saveImageToPrivateStorage(uri)
    }
}