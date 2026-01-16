package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.playlistmaker.favorites.data.db.TrackDao
import com.practicum.playlistmaker.favorites.data.db.TrackEntity
import com.practicum.playlistmaker.playlist.data.convertors.TrackIdConvertor
import com.practicum.playlistmaker.playlist.data.db.PlaylistDao
import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class])
@TypeConverters(TrackIdConvertor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao

}