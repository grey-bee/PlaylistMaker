package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.model.Track

class GetSearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    GetSearchHistoryInteractor {
    override operator fun invoke(): List<Track> {
        return repository.getHistory()
    }
}