package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryAddInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.model.Track

class HistoryAddInteractorImpl(private val repository: HistoryRepository) :
    HistoryAddInteractor {
    override suspend operator fun invoke(track: Track) {
        repository.addToHistory(track)
    }
}