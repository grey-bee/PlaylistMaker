package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.Email
import com.practicum.playlistmaker.sharing.domain.model.Share
import com.practicum.playlistmaker.sharing.domain.model.Url

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val themeLiveData =
        MutableLiveData<Boolean>(settingsInteractor.getThemeSettings().isDarkTheme)

    fun observeTheme(): LiveData<Boolean> = themeLiveData
    fun changeDarkTheme(value: Boolean) {
        settingsInteractor.saveThemeSettings(ThemeSettings(value))
        themeLiveData.postValue(value)
    }

    fun shareApp(): Share {
        return sharingInteractor.shareApp()
    }

    fun writeToSupport(): Email {
        return sharingInteractor.writeSupport()
    }


    fun userAgreement(): Url {
        return sharingInteractor.userAgreement()
    }
}