package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.ui.AudioPlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { MediaPlayer() }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { (track: Track) -> AudioPlayerViewModel(track, get()) }
}