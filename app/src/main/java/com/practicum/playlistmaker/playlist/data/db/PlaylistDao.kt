package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Delete()
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    fun getPlaylists(): Flow<List<PlaylistEntity>>
}