package com.practicum.playlistmaker.domain.api

interface SettingsRepository {
    fun saveDarkTheme(state: Boolean)
    fun getDarkTheme(): Boolean
}