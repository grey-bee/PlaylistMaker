package com.practicum.playlistmaker.ui.mock

import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track

object PreviewData {
    val track = Track(
        trackId = "1441164805",
        trackName = "Yesterday",
        artistName = "The Beatles",
        trackTimeMillis = 125667,
        artworkUrl100 = "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/1a/19/db/1a19db26-17ad-b986-11a9-f72ac7a6194b/18UMGIM31214.rgb.jpg/100x100bb.jpg",
        collectionName = "Help!",
        releaseYear = "1965",
        primaryGenreName = "Rock",
        country = "USA",
        previewUrl = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview221/v4/d1/69/2d/d1692d74-fe32-c676-7a1d-00deacae1644/mzaf_11316115358642175957.plus.aac.p.m4a",
        artworkUrl512 = "https://is1-ssl.mzstatic.com/image/thumb/Music122/v4/1a/19/db/1a19db26-17ad-b986-11a9-f72ac7a6194b/18UMGIM31214.rgb.jpg/512x512bb.jpg",
        isFavorite = true
    )

    val trackList10 =
        listOf(track, track, track, track, track, track, track, track, track, track)
    val trackList5 =
        listOf(track, track, track, track, track)

    val playlist = Playlist(
        id = 1L,
        name = "Мой плейлист",
        description = "Любимые треки",
        imagePath = null,
        trackIds = listOf("1", "2", "3"),
        trackCount = 3
    )

    val playlistList10 = listOf(
        playlist,
        playlist,
        playlist,
        playlist,
        playlist,
        playlist,
        playlist,
        playlist,
        playlist,
        playlist
    )
    val playlistList5 = listOf(playlist, playlist, playlist, playlist, playlist)
}