package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun saveImageToPrivateStorage(uri: Uri): String
}