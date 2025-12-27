package com.practicum.playlistmaker.old.creator.domain.impl

import com.practicum.playlistmaker.domain.api.GetDarkThemeInteractor
import com.practicum.playlistmaker.domain.api.SettingsRepository

class GetDarkThemeInteractorImpl(private val repository: SettingsRepository): GetDarkThemeInteractor {
    override operator fun invoke(): Boolean {
        return repository.getDarkTheme()
    }
}