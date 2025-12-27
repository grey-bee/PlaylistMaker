package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track

interface TracksRepository {
    fun search(query: String): List<Track>
}