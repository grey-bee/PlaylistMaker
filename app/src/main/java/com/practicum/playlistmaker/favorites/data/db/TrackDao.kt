package com.practicum.playlistmaker.favorites.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: TrackEntity)

    @Query("DELETE FROM favorites WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM favorites")
    suspend fun getTracksId(): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE trackId = :trackId)")
    suspend fun isFavorite(trackId: String): Boolean
}