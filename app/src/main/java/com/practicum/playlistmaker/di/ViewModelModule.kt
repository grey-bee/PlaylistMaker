package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.favorites.ui.FavoritesViewModel
import com.practicum.playlistmaker.medialibrary.ui.PlaylistsViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.search.ui.SearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { MediaPlayer() }
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get(), get()) }
    viewModel { (track: Track) -> PlayerViewModel(track, get(), get()) }
    viewModel { PlaylistsViewModel() }
    viewModel { FavoritesViewModel(get()) }
}