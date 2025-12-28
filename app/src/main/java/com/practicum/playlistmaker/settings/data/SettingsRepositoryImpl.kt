package com.practicum.playlistmaker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences,
    private val themeSettingsKey: String,
    private val gson: Gson
) :
    SettingsRepository {

    override fun saveThemeSettings(themeSettings: ThemeSettings) {
        val json = gson.toJson(themeSettings)
        sharedPrefs.edit { putString(themeSettingsKey, json) }
        applyTheme(themeSettings.isDarkTheme)
    }

    override fun getThemeSettings(): ThemeSettings {
        val json = sharedPrefs.getString(themeSettingsKey, null)
        return json?.let { gson.fromJson(it, ThemeSettings::class.java) } ?: ThemeSettings()
    }

    override fun applyTheme(isDarkTheme: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}