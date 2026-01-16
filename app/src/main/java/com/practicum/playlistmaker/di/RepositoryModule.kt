package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorites.data.FavoritesRepositoryImpl
import com.practicum.playlistmaker.favorites.data.convertors.TrackDbConvertor
import com.practicum.playlistmaker.favorites.domain.FavoritesRepository
import com.practicum.playlistmaker.playlist.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist.data.convertors.PlaylistDbConvertor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.search.data.repository.HistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
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
    single { TrackDbConvertor() }
    single { PlaylistDbConvertor() }

    single<HistoryRepository> {
        HistoryRepositoryImpl(
            get(named("history_prefs")),
            get(), get()
        )
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(
            get(named("settings_prefs")),
            THEME_SETTINGS,
            get()
        )
    }

    single<TracksRepository> { TracksRepositoryImpl(get(), get()) }
    single<SharingRepository> { SharingRepositoryImpl(androidContext()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get()) }
    single<PlaylistRepository> { PlaylistRepositoryImpl(get(), get()) }
}