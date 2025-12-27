package com.practicum.playlistmaker.old.creator.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface AddTrackToHistoryInteractor {
    operator fun invoke(track: Track)
}