package com.practicum.playlistmaker

import android.content.Context
import android.util.TypedValue
import androidx.appcompat.app.AppCompatDelegate

fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}

fun applyTheme(isDarkTheme: Boolean) {
    AppCompatDelegate.setDefaultNightMode(
        if (isDarkTheme) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
    )
}
