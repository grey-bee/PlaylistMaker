package com.practicum.playlistmaker.old.creator.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface TracksRepository {
    fun search(query: String): List<Track>
}