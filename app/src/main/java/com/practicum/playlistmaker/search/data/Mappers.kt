package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun Int.toTimeString(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}.format(this)

fun String.toArtworkUrl512(): String = this.replaceAfterLast('/', "512x512bb.jpg")
fun String.takeYear() = this.take(4)

fun TrackDto.toTrack(isFavorite: Boolean = false): Track = Track(
    this.trackId,
    this.trackName,
    this.artistName,
    this.trackTimeMillis.toTimeString(),
    this.artworkUrl100,
    this.artworkUrl100.toArtworkUrl512(),
    this.collectionName,
    this.releaseDate.takeYear(),
    this.primaryGenreName,
    this.country,
    this.previewUrl,
    isFavorite
)

