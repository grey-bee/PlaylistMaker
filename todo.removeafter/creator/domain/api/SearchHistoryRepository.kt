package com.practicum.playlistmaker.old.creator.domain.api

import com.practicum.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun getHistory(): List<Track>
    fun addToHistory(track: Track)
    fun clearHistory()
}