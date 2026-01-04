package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.domain.api.SharingRepository
import com.practicum.playlistmaker.sharing.data.SharingRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


private const val THEME_SETTINGS = "theme_settings"

val repositoryModule = module {
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(
            get(named("history_prefs")),
            get()
        )
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(
            get(named("settings_prefs")),
            THEME_SETTINGS,
            get()
        )
    }
    single<TracksRepository> { TracksRepositoryImpl(get()) }

    single<SharingRepository> { SharingRepositoryImpl(androidContext()) }
}