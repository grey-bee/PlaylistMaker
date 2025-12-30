package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsInteractor {
    fun saveThemeSettings(themeSettings: ThemeSettings)
    fun getThemeSettings(): ThemeSettings
    fun applyCurrentTheme()
}