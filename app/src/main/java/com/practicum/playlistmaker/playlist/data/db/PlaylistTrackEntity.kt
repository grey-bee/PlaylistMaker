package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tracks")
data class PlaylistTrackEntity (
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
    val trackTime: String,
    val previewUrl: String,
    val addedAt: Long
)