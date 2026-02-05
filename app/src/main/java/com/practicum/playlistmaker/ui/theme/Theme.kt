package com.practicum.playlistmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalTheme = staticCompositionLocalOf { LightCustomColors }

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkCustomColors else LightCustomColors

    CompositionLocalProvider(LocalTheme provides colorScheme) {
        MaterialTheme(
            typography = PlaylistTypography,
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            content = content
        )
    }
}

private val LightColorScheme = lightColorScheme(
    primary = MainBlue,
    background = White,
    surface = White,
    onSurface = DarkGrey,
    onBackground = LightGrey,
)
private val DarkColorScheme = darkColorScheme(
    primary = DarkGrey,
    background = DarkGrey,
    surface = DarkGrey,
    onSurface = White,
    onBackground = White,
)

data class CustomColors(
    val switchThumb: Color,
    val switchTrack: Color,
)

val LightCustomColors = CustomColors(
    switchThumb = LightGrey,
    switchTrack = VeryLightGrey,
)

val DarkCustomColors = CustomColors(
    switchThumb = MainBlue,
    switchTrack = LightBlue.copy(alpha = 0.48f),
)