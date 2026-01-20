package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylist(): Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun saveImageToPrivateStorage(uri: Uri): String
    suspend fun addPlaylistTrack(track: Track)
    suspend fun deletePlaylistTrack(track: Track)
    suspend fun getPlaylistTrack(trackId: String): Track?

}