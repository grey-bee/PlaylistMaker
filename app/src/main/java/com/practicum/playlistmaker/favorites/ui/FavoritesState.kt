package com.practicum.playlistmaker.favorites.ui

import com.practicum.playlistmaker.search.domain.model.Track

sealed interface FavoritesState {
    data class Content(
        val tracks: List<Track>
    ) : FavoritesState

    data object Empty : FavoritesState
}