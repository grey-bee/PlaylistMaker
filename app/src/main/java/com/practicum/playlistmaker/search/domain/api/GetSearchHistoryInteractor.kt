package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track

interface GetSearchHistoryInteractor {
    operator fun invoke(): List<Track>
}