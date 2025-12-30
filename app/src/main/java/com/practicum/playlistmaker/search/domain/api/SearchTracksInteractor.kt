package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track

interface SearchTracksInteractor {
    operator fun invoke(query: String, consumer: Consumer)

    interface Consumer {
        fun consume(foundTracks: List<Track>?)
    }
}