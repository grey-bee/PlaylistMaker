package com.practicum.playlistmaker.old.creator.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface GetSearchHistoryInteractor {
    operator fun invoke(): List<Track>
}