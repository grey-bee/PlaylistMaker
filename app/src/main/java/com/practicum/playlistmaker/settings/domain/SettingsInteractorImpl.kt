package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun saveThemeSettings(themeSettings: ThemeSettings) {
        settingsRepository.saveThemeSettings(themeSettings)
    }

    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun applyCurrentTheme() {
        settingsRepository.applyTheme(getThemeSettings().isDarkTheme)
    }
}