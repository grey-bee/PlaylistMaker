package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    private val getDarkThemeInteractor by lazy { Creator.provideGetDarkThemeInteractor(this) }
    private val setDarkThemeInteractor by lazy { Creator.provideSaveDarkThemeInteractor(this) }
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        darkTheme = getDarkThemeInteractor.execute()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        setDarkThemeInteractor.execute(darkThemeEnabled)
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}