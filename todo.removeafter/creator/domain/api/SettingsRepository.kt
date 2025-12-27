package com.practicum.playlistmaker.old.creator.domain.api

interface SettingsRepository {
    fun saveDarkTheme(state: Boolean)
    fun getDarkTheme(): Boolean
}