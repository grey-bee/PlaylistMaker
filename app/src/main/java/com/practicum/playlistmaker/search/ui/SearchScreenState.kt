package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface SearchScreenState {
    data class Content(val tracks: List<Track>) : SearchScreenState
    data class History(val tracks: List<Track>) : SearchScreenState
    object Loading : SearchScreenState
    object Error : SearchScreenState
    object Empty : SearchScreenState
}