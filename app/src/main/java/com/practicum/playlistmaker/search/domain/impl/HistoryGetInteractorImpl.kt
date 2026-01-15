package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryGetInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.model.Track

class HistoryGetInteractorImpl(private val repository: HistoryRepository) :
    HistoryGetInteractor {
    override suspend operator fun invoke(): List<Track> {
        return repository.getHistory()
    }
}