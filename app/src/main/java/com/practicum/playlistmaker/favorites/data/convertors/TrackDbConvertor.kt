package com.practicum.playlistmaker.favorites.data.convertors

import com.practicum.playlistmaker.favorites.data.db.TrackEntity
import com.practicum.playlistmaker.search.domain.model.Track

class TrackDbConvertor {
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.artworkUrl100,
            track.artworkUrl512,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
            track.trackTime,
            track.previewUrl,
            System.currentTimeMillis()
        )
    }

    fun map(entity: TrackEntity): Track {
        return Track(
            entity.trackId,
            entity.trackName,
            entity.artistName,
            entity.trackTime,
            entity.artworkUrl100,
            entity.artworkUrl512,
            entity.collectionName,
            entity.releaseYear,
            entity.primaryGenreName,
            entity.country,
            entity.previewUrl,
            true
        )
    }
}