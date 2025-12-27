package com.practicum.playlistmaker.search.ui

import android.os.Message
import com.practicum.playlistmaker.search.domain.model.Track

sealed interface SearchScreenState {
    object Loading : SearchScreenState
    data class Content(val tracks: List<Track>) : SearchScreenState
    object Error : SearchScreenState
    object Empty : SearchScreenState
}