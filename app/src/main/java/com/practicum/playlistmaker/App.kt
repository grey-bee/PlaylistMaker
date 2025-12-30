package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator

class App : Application() {
    private val getThemeSettings by lazy { Creator.provideSettingsInteractor(this) }
    override fun onCreate() {
        super.onCreate()
        getThemeSettings.applyCurrentTheme()
    }
}