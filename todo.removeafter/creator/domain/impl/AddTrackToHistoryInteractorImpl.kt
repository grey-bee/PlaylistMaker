package com.practicum.playlistmaker.old.creator.domain.impl

import com.practicum.playlistmaker.domain.api.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class AddTrackToHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    AddTrackToHistoryInteractor {
    override  operator fun invoke(track: Track) {
        repository.addToHistory(track)
    }
}