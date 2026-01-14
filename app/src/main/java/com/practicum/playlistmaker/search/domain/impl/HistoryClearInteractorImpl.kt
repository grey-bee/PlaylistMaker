package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.HistoryClearInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryRepository

class HistoryClearInteractorImpl(private val repository: HistoryRepository) :
    HistoryClearInteractor {
    override operator fun invoke() {
        repository.clearHistory()
    }
}