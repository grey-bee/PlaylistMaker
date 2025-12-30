package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsRepository {
    fun saveThemeSettings(themeSettings: ThemeSettings)
    fun getThemeSettings(): ThemeSettings
    fun applyTheme(isDarkTheme: Boolean)

}