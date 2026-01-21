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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.math.min

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

    override fun getPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getPlaylists()
            .map { entities -> entities.map { entity -> convertEntityToPlaylist(entity) } }
    }

    override suspend fun getPlaylistById(id: Long): Playlist {
        val entity = appDatabase.playlistDao().getPlaylistById(id)
        return convertEntityToPlaylist(entity)
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
                    val bitmap = BitmapFactory.decodeStream(input)
                    val h = bitmap.height
                    val w = bitmap.width
                    val minSize = min(w, h)
                    Bitmap.createBitmap(
                        bitmap,
                        (w - minSize) / 2,
                        (h - minSize) / 2,
                        minSize,
                        minSize
                    )
                        .compress(Bitmap.CompressFormat.JPEG, 30, output)
                }
            }
            file.absolutePath
        }
    }

    override suspend fun addPlaylistTrack(track: Track) {
        appDatabase.playlistTrackDao().addPlaylistTrack(convertPlaylistTrackToEntity(track))
    }

    override suspend fun deletePlaylistTrack(track: Track) {
        val playlists = getPlaylists().first()
        if (!playlists.any { it.trackIds.contains(track.trackId) }) {
            appDatabase.playlistTrackDao().deletePlaylistTrack(convertPlaylistTrackToEntity(track))
        }
    }

    override suspend fun getPlaylistTrack(trackId: String): Track? {
        val entity = appDatabase.playlistTrackDao().getPlaylistTrack(trackId)
        return entity?.let { convertEntityToPlaylistTrack(it) }
    }

    override suspend fun getPlaylistTracks(trackIds: List<String>): List<Track> {
        val entities = appDatabase.playlistTrackDao().getPlaylistTracks(trackIds)
        return entities.map { entity -> convertEntityToPlaylistTrack(entity) }
    }

    override suspend fun deleteTrackFromPlaylist(
        track: Track,
        playlist: Playlist
    ) {
        val updatedTrackIds = playlist.trackIds.filter { it != track.trackId }
        val updatedPlaylist = playlist.copy(trackIds = updatedTrackIds, trackCount = updatedTrackIds.size)
        updatePlaylist(updatedPlaylist)
        deletePlaylistTrack(track)
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