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
    onPrimary = LightGrey,
    primaryContainer = VeryLightGrey,
    onSecondary = Black
)
private val DarkColorScheme = darkColorScheme(
    primary = DarkGrey,
    background = DarkGrey,
    surface = DarkGrey,
    onSurface = White,
    onBackground = White,
    onPrimary = Black,
    primaryContainer = White,
    onSecondary = White
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
//Primary группа:
//- onPrimaryContainer — контент на primaryContainer
//
//Secondary группа:
//- secondary, secondaryContainer, onSecondaryContainer
//
//Tertiary группа:
//- tertiary, onTertiary, tertiaryContainer, onTertiaryContainer
//
//Error группа:
//- error, onError, errorContainer, onErrorContainer
//
//Surface варианты:
//- surfaceVariant, onSurfaceVariant
//- surfaceTint
//- inverseSurface, inverseOnSurface
//
//Другие:
//- outline — для рамок, разделителей
//- outlineVariant — более мягкий outline
//- scrim — затемнение фона (для диалогов)
//- inversePrimary