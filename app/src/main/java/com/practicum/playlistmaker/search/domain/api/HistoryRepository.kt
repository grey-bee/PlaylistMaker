package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.model.Track

interface HistoryRepository {
    suspend fun getHistory(): List<Track>
    suspend fun addToHistory(track: Track)
    fun clearHistory()
}