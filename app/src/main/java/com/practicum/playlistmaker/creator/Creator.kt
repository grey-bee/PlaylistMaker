package com.practicum.playlistmaker.creator

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.data.network.RetrofitClient
import com.practicum.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.domain.api.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.domain.api.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.GetDarkThemeInteractor
import com.practicum.playlistmaker.domain.api.GetSearchHistoryInteractor
import com.practicum.playlistmaker.domain.api.SaveDarkThemeInteractor
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.domain.api.SettingsRepository
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.AddTrackToHistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.ClearSearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.GetDarkThemeInteractorImpl
import com.practicum.playlistmaker.domain.impl.GetSearchHistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.SaveDarkThemeInteractorImpl
import com.practicum.playlistmaker.domain.impl.SearchTracksInteractorImpl

object Creator {
    private const val KEY_PREFS = "app_prefs"
    private const val KEY_HISTORY = "history"
    private val networkClient = RetrofitClient()

    fun provideSearchTracksInteractor(): SearchTracksInteractor {
        return SearchTracksInteractorImpl(getTracksRepository())
    }

    fun provideGetSearchHistoryInteractor(context: Context): GetSearchHistoryInteractor {
        return GetSearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    fun provideAddTrackToHistoryInteractor(context: Context): AddTrackToHistoryInteractor {
        return AddTrackToHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    fun provideClearSearchHistoryInteractor(context: Context): ClearSearchHistoryInteractor {
        return ClearSearchHistoryInteractorImpl(getSearchHistoryRepository(context))
    }

    fun provideGetDarkThemeInteractor(context: Context): GetDarkThemeInteractor {
        return GetDarkThemeInteractorImpl(getSettingsRepository(context))
    }

    fun provideSaveDarkThemeInteractor(context: Context): SaveDarkThemeInteractor {
        return SaveDarkThemeInteractorImpl(getSettingsRepository(context))
    }


    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(networkClient)
    }

    private fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(
            context.getSharedPreferences(
                KEY_HISTORY, Context.MODE_PRIVATE
            ), Gson()
        )
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        )
    }

}