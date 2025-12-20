package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository

class ClearSearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    ClearSearchHistoryInteractor {
    override fun execute() {
        repository.clearHistory()
    }
}