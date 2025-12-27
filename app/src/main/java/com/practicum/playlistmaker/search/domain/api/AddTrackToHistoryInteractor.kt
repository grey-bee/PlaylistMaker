package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track

interface AddTrackToHistoryInteractor {
    operator fun invoke(track: Track)
}