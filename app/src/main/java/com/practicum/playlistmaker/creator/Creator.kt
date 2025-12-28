package com.practicum.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmaker.search.data.network.RetrofitClient
import com.practicum.playlistmaker.search.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.api.TracksRepository
import com.practicum.playlistmaker.search.domain.impl.AddTrackToHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.ClearSearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.GetSearchHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.SearchTracksInteractorImpl
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.data.SharingInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

object Creator {
    private const val KEY_PREFS = "app_prefs"
    private const val KEY_HISTORY = "history"
    private val networkClient = RetrofitClient()
    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(context)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(
            getTracksRepository()
        )
    }

    fun provideAddTrackToHistoryInteractor(context: Context): AddTrackToHistoryInteractor {
        return AddTrackToHistoryInteractorImpl(
            getSearchHistoryRepository(context)
        )
    }

    fun provideGetSearchHistoryInteractor(context: Context): GetSearchHistoryInteractor {
        return GetSearchHistoryInteractorImpl(
            getSearchHistoryRepository(context)
        )
    }

    fun provideClearSearchHistoryInteractor(context: Context): ClearSearchHistoryInteractor {
        return ClearSearchHistoryInteractorImpl(
            getSearchHistoryRepository(context)
        )
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            context.getSharedPreferences(
                KEY_PREFS, Context.MODE_PRIVATE
            )
        )
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(networkClient)
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            context.getSharedPreferences(
                KEY_HISTORY, Context.MODE_PRIVATE
            )
        )
    }

    fun provideMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }
}