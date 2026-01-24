package com.practicum.playlistmaker.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun Int.toTimeString(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}.format(this)