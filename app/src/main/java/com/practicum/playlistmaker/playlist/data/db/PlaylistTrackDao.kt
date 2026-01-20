package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylistTrack(playlistTrack: PlaylistTrackEntity)

    @Delete
    suspend fun deletePlaylistTrack(playlistTrack: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun getPlaylistTrack(trackId: String): PlaylistTrackEntity?
}