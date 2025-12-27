package com.practicum.playlistmaker.settings.ui

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    fun isDarkTheme(): Boolean {
        return settingsInteractor.getThemeSettings().isDarkTheme
    }

    fun changeDarkTheme(value: Boolean) {
        settingsInteractor.saveThemeSettings(ThemeSettings(value))
    }

    fun shareApp(): Intent {
        return sharingInteractor.shareApp()
    }

    fun writeToSupport(): Intent {
        return sharingInteractor.writeSupport()
    }


    fun userAgreement(): Intent {
        return sharingInteractor.userAgreement()
    }

    companion object {
        fun getFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    Creator.provideSettingsInteractor(context),
                    Creator.provideSharingInteractor(context)
                )
            }

        }
    }
}