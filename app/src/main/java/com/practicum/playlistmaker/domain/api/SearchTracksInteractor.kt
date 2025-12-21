package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SearchTracksInteractor {
    operator fun invoke(query: String, consumer: Consumer)

    interface Consumer {
        fun consume(foundTracks: List<Track>?)
    }
}