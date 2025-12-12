package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class Track(
    val trackId : String,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
) {
    val trackTime: String
        get() = SimpleDateFormat("m:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(trackTimeMillis)
}