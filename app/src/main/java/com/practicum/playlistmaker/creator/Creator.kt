package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.settings.data.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.data.SharingInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

object Creator {
    private const val KEY_PREFS = "app_prefs"
    private const val KEY_HISTORY = "history"
//    private val networkClient = RetrofitClient()

//    fun provideSearchTracksInteractor(): com.practicum.playlistmaker.old.creator.domain.api.SearchTracksInteractor {
//        return _root_ide_package_.com.practicum.playlistmaker.old.creator.domain.impl.SearchTracksInteractorImpl(
//            getTracksRepository()
//        )
//    }

//    fun provideGetSearchHistoryInteractor(context: Context): com.practicum.playlistmaker.old.creator.domain.api.GetSearchHistoryInteractor {
//        return _root_ide_package_.com.practicum.playlistmaker.old.creator.domain.impl.GetSearchHistoryInteractorImpl(
//            getSearchHistoryRepository(context)
//        )
//    }

    //    fun provideAddTrackToHistoryInteractor(context: Context): com.practicum.playlistmaker.old.creator.domain.api.AddTrackToHistoryInteractor {
//        return _root_ide_package_.com.practicum.playlistmaker.old.creator.domain.impl.AddTrackToHistoryInteractorImpl(
//            getSearchHistoryRepository(context)
//        )
//    }
//
//    fun provideClearSearchHistoryInteractor(context: Context): com.practicum.playlistmaker.old.creator.domain.api.ClearSearchHistoryInteractor {
//        return _root_ide_package_.com.practicum.playlistmaker.old.creator.domain.impl.ClearSearchHistoryInteractorImpl(
//            getSearchHistoryRepository(context)
//        )
//    }
//
//    fun provideGetDarkThemeInteractor(context: Context): com.practicum.playlistmaker.old.creator.domain.api.GetDarkThemeInteractor {
//        return _root_ide_package_.com.practicum.playlistmaker.old.creator.domain.impl.GetDarkThemeInteractorImpl(
//            getSettingsRepository(context)
//        )
//    }
//
//    fun provideSaveDarkThemeInteractor(context: Context): com.practicum.playlistmaker.old.creator.domain.api.SaveDarkThemeInteractor {
//        return _root_ide_package_.com.practicum.playlistmaker.old.creator.domain.impl.SaveDarkThemeInteractorImpl(
//            getSettingsRepository(context)
//        )
//    }
//
//
//    private fun getTracksRepository(): com.practicum.playlistmaker.old.creator.domain.api.TracksRepository {
//        return TracksRepositoryImpl(networkClient)
//    }
//
//    private fun getSearchHistoryRepository(context: Context): com.practicum.playlistmaker.old.creator.domain.api.SearchHistoryRepository {
//        return SearchHistoryRepositoryImpl(
//            context.getSharedPreferences(
//                KEY_HISTORY, Context.MODE_PRIVATE
//            ), Gson()
//        )
//    }
    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        )
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(context)
    }

}