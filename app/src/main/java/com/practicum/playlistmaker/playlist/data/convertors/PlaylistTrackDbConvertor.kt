package com.practicum.playlistmaker.playlist.data.convertors

import com.practicum.playlistmaker.playlist.data.db.PlaylistTrackEntity
import com.practicum.playlistmaker.search.domain.model.Track

class PlaylistTrackDbConvertor {
    fun map(track: Track): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            track.trackId,
            track.artworkUrl100,
            track.artworkUrl512,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
            track.trackTimeMillis,
            track.previewUrl,
            System.currentTimeMillis(),
        )
    }

    fun map(entity: PlaylistTrackEntity): Track {
        return Track(
            entity.trackId,
            entity.trackName,
            entity.artistName,
            entity.trackTimeMillis,
            entity.artworkUrl100,
            entity.artworkUrl512,
            entity.collectionName,
            entity.releaseYear,
            entity.primaryGenreName,
            entity.country,
            entity.previewUrl
        )
    }
}