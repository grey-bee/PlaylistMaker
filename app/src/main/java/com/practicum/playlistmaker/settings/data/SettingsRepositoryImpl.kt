package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences) :
    SettingsRepository {

    override fun saveThemeSettings(themeSettings: ThemeSettings) {
        val json = gson.toJson(themeSettings)
        sharedPrefs.edit { putString(THEME_SETTINGS, json) }
    }

    override fun getThemeSettings(): ThemeSettings {
        val json = sharedPrefs.getString(THEME_SETTINGS, null)
        return json?.let { gson.fromJson(it, ThemeSettings::class.java) } ?: ThemeSettings()
    }

    companion object {
        private const val THEME_SETTINGS = "theme_settings"
        private val gson = Gson()
    }

}