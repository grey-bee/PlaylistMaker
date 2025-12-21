package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.SaveDarkThemeInteractor
import com.practicum.playlistmaker.domain.api.SettingsRepository

class SaveDarkThemeInteractorImpl(private val repository: SettingsRepository) :
    SaveDarkThemeInteractor {
    override operator fun invoke(state: Boolean) {
        repository.saveDarkTheme(state)
    }
}