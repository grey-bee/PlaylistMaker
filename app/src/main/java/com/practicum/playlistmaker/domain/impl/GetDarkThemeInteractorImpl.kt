package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.GetDarkThemeInteractor
import com.practicum.playlistmaker.domain.api.SettingsRepository

class GetDarkThemeInteractorImpl(private val repository: SettingsRepository): GetDarkThemeInteractor {
    override fun execute(): Boolean {
        return repository.getDarkTheme()
    }
}