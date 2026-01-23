package com.practicum.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.ApiService
import com.practicum.playlistmaker.search.data.network.RetrofitClient
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://itunes.apple.com"
private const val KEY_PREFS = "app_prefs"
private const val KEY_HISTORY = "history"


val dataModule = module {
    single { Gson() }

    single(named("settings_prefs")) {
        androidContext().getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
    }
    single(named("history_prefs")) {
        androidContext().getSharedPreferences(KEY_HISTORY, Context.MODE_PRIVATE)
    }

    single<ApiService> {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()

        retrofit.create(ApiService::class.java)
    }

    single<NetworkClient> { RetrofitClient(get()) }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}