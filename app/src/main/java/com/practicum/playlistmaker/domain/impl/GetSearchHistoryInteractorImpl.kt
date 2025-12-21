package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.GetSearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class GetSearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    GetSearchHistoryInteractor {
    override operator fun invoke(): List<Track> {
        return repository.getHistory()
    }
}