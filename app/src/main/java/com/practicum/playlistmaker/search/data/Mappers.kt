package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.model.Track

fun String.toArtworkUrl512(): String = this.replaceAfterLast('/', "512x512bb.jpg")
fun String.takeYear() = this.take(4)

fun TrackDto.toTrack(isFavorite: Boolean = false): Track = Track(
    this.trackId,
    this.trackName,
    this.artistName,
    this.trackTimeMillis,
    this.artworkUrl100,
    this.artworkUrl100.toArtworkUrl512(),
    this.collectionName,
    this.releaseDate.takeYear(),
    this.primaryGenreName,
    this.country,
    this.previewUrl ?: "",
    isFavorite
)

