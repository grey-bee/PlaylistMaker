package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface AddTrackToHistoryInteractor {
    operator fun invoke(track: Track)
}