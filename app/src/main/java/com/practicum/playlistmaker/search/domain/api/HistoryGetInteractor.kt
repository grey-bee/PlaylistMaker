package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track

interface HistoryGetInteractor {
    suspend operator fun invoke(): List<Track>
}