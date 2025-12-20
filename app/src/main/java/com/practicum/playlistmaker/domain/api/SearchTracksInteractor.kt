package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SearchTracksInteractor {
    fun execute(query: String, consumer: Consumer)

    interface Consumer {
        fun consume(foundTracks: List<Track>?)
    }
}