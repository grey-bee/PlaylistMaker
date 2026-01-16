package com.practicum.playlistmaker.playlist.data.convertors

import com.practicum.playlistmaker.playlist.data.db.PlaylistEntity
import com.practicum.playlistmaker.playlist.domain.model.Playlist

class PlaylistDbConvertor {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.imagePath,
            playlist.trackIds,
            playlist.trackCount
        )
    }

    fun map(entity: PlaylistEntity): Playlist {
        return Playlist(
            entity.id,
            entity.name,
            entity.description,
            entity.imagePath,
            entity.trackIds,
            entity.trackCount
        )
    }
}