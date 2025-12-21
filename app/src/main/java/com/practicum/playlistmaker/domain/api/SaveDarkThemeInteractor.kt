package com.practicum.playlistmaker.domain.api

interface SaveDarkThemeInteractor {
    operator fun invoke(state: Boolean)
}