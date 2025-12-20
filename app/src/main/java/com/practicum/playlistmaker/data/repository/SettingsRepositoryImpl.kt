package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.practicum.playlistmaker.domain.api.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {
    override fun getDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_THEME, false)
    }

    override fun saveDarkTheme(state: Boolean) {
        sharedPreferences.edit {
            putBoolean(KEY_DARK_THEME, state)
        }
    }

    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
    }
}