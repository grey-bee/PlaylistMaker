package com.practicum.playlistmaker.old.creator.domain.settings

import com.practicum.playlistmaker.domain.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}