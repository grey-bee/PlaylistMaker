package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackDto
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun Int.toTimeString(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}.format(this)
fun String.toArtworkUrl512(): String = this.replaceAfterLast('/', "512x512bb.jpg")
fun String.takeYear() = this.take(4)

fun TrackDto.toTrack(): Track = Track(
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
    this.previewUrl
)

