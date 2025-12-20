package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class AddTrackToHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    AddTrackToHistoryInteractor {
    override fun execute(track: Track) {
        repository.addToHistory(track)
    }
}