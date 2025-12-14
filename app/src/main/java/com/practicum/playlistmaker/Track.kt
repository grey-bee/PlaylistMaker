package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    val trackId: String,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название Альбома — может не быть
    val releaseDate: String, // Год релиза трека — может не быть
    val primaryGenreName: String, // Жанр
    val country: String // Страна
) : Parcelable {
    val trackTime: String
        get() = SimpleDateFormat("m:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(trackTimeMillis)
    val artworkUrl512: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    val year: String
        get() = releaseDate.take(4)

}