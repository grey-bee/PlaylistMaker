package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import org.koin.dsl.module

val viewModelModule = module {
    factory { MediaPlayer() }
}