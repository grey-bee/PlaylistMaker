package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.model.Track

class AddTrackToHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    AddTrackToHistoryInteractor {
    override  operator fun invoke(track: Track) {
        repository.addToHistory(track)
    }
}