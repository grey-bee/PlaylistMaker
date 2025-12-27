package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository

class ClearSearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    ClearSearchHistoryInteractor {
    override operator fun invoke() {
        repository.clearHistory()
    }
}