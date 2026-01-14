package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorites.domain.FavoritesInteractor
import com.practicum.playlistmaker.favorites.domain.FavoritesInteractorImpl
import com.practicum.playlistmaker.search.domain.api.HistoryAddInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryClearInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryGetInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.HistoryAddInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.HistoryClearInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.HistoryGetInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<TracksInteractor> { TracksInteractorImpl(get()) }
    single<HistoryGetInteractor> { HistoryGetInteractorImpl(get()) }
    single<HistoryAddInteractor> { HistoryAddInteractorImpl(get()) }
    single<HistoryClearInteractor> { HistoryClearInteractorImpl(get()) }
    single<SettingsInteractor> { SettingsInteractorImpl(get()) }
    single<SharingInteractor> { SharingInteractorImpl(get()) }
    single<FavoritesInteractor> { FavoritesInteractorImpl(get()) }
}