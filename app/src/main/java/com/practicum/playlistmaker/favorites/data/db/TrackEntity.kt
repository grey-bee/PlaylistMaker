package com.practicum.playlistmaker.favorites.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class TrackEntity(
    @PrimaryKey
    val trackId: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: Int,
    val previewUrl: String,
    val addedAt: Long
)