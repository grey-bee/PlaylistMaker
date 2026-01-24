package com.practicum.playlistmaker.playlist.domain

import android.net.Uri
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistById(id: Long): Playlist
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun saveImageToPrivateStorage(uri: Uri): String
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
    suspend fun deletePlaylistTrack(track: Track)
    suspend fun getPlaylistTrack(trackId: String): Track?
    suspend fun getPlaylistTracks(trackIds: List<String>): List<Track>
    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist)
}