package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track

interface HistoryAddInteractor {
    suspend operator fun invoke(track: Track)
}