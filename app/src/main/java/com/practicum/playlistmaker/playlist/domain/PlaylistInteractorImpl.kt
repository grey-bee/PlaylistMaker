package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
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

    override suspend fun updatePlaylist(playlist: Playlist) {
        return repository.updatePlaylist(playlist)
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): String {
        return repository.saveImageToPrivateStorage(uri)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        return repository.addTrackToPlaylist(track, playlist)
    }

    override suspend fun deletePlaylistTrack(track: Track) {
        return repository.deletePlaylistTrack(track)
    }

    override suspend fun getPlaylistTrack(trackId: String): Track? {
        return repository.getPlaylistTrack(trackId)
    }
}