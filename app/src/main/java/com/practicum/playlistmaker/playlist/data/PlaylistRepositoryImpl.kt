package com.practicum.playlistmaker.playlist.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.playlist.data.convertors.PlaylistDbConvertor
import com.practicum.playlistmaker.playlist.data.convertors.PlaylistTrackDbConvertor
import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.data.db.PlaylistTrackEntity
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistConvertor: PlaylistDbConvertor,
    private val trackConvertor: PlaylistTrackDbConvertor,
    private val context: Context
) :
    PlaylistRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().addPlaylist(convertPlaylistToEntity(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(convertPlaylistToEntity(playlist))
    }

    override fun getPlaylist(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists()
            .map { entities -> entities.map { entity -> convertEntityToPlaylist(entity) } }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        return appDatabase.playlistDao().updatePlaylist(convertPlaylistToEntity(playlist))
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri): String {
        return withContext(Dispatchers.IO) {
            val dir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "playlist_cover"
            )
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, "${UUID.randomUUID()}.jpg")
            context.contentResolver.openInputStream(uri).use { input ->
                FileOutputStream(file).use { output ->
                    BitmapFactory.decodeStream(input)
                        .compress(Bitmap.CompressFormat.JPEG, 30, output)
                }
            }
            file.absolutePath
        }
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        updatePlaylist(
            playlist.copy(
                trackIds = playlist.trackIds + track.trackId,
                trackCount = playlist.trackCount + 1
            )
        )
        appDatabase.playlistTrackDao().addPlaylistTrack(convertPlaylistTrackToEntity(track))
    }

    override suspend fun deletePlaylistTrack(track: Track) {
        appDatabase.playlistTrackDao().deletePlaylistTrack(convertPlaylistTrackToEntity(track))
    }

    override suspend fun getPlaylistTrack(trackId: String): Track? {
        val entity = appDatabase.playlistTrackDao().getPlaylistTrack(trackId)
        return entity?.let { convertEntityToPlaylistTrack(it) }
    }

    private fun convertEntityToPlaylist(entity: PlaylistEntity): Playlist {
        return playlistConvertor.map(entity)
    }

    private fun convertPlaylistToEntity(playlist: Playlist): PlaylistEntity {
        return playlistConvertor.map(playlist)
    }

    private fun convertEntityToPlaylistTrack(entity: PlaylistTrackEntity): Track {
        return trackConvertor.map(entity)
    }

    private fun convertPlaylistTrackToEntity(track: Track): PlaylistTrackEntity {
        return trackConvertor.map(track)
    }
}