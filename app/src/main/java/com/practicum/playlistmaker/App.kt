package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

class App : Application() {
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        SearchHistoryManager.initialize(this)
        darkTheme = sharedPrefs.getBoolean("dark_theme", false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        getSharedPreferences("app_prefs", MODE_PRIVATE)
            .edit {
                putBoolean("dark_theme", darkThemeEnabled)
            }

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}