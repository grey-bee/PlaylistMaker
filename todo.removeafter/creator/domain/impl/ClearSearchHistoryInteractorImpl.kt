package com.practicum.playlistmaker.old.creator.domain.impl

import com.practicum.playlistmaker.domain.api.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository

class ClearSearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    ClearSearchHistoryInteractor {
    override operator fun invoke() {
        repository.clearHistory()
    }
}