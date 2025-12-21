package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface GetSearchHistoryInteractor {
    operator fun invoke(): List<Track>
}