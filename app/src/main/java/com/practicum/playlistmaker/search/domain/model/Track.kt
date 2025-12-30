package com.practicum.playlistmaker.search.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val artworkUrl512: String,
    val collectionName: String,
    val releaseYear: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Parcelable