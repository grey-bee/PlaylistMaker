package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.domain.api.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.AddTrackToHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.ClearSearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.GetSearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<TracksInteractor> { TracksInteractorImpl(get()) }
    single<GetSearchHistoryInteractor> { GetSearchHistoryInteractorImpl(get()) }
    single<AddTrackToHistoryInteractor> { AddTrackToHistoryInteractorImpl(get()) }
    single<ClearSearchHistoryInteractor> { ClearSearchHistoryInteractorImpl(get()) }
    single<SettingsInteractor> { SettingsInteractorImpl(get()) }
    single<SharingInteractor> { SharingInteractorImpl(get()) }
}