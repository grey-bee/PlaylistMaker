package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface AddTrackToHistoryInteractor {
    fun execute(track: Track)
}