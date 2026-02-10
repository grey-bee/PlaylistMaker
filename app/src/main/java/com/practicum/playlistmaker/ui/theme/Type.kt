package com.practicum.playlistmaker.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.practicum.playlistmaker.R
import  androidx.compose.ui.text.TextStyle
import  androidx.compose.material3.Typography
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.sp


val YsFamily = FontFamily(
    Font(R.font.ys_display_regular, FontWeight.Normal),
    Font(R.font.ys_display_medium, FontWeight.Medium),
    Font(R.font.ys_display_bold, FontWeight.Bold)
)

val PlaylistTypography = Typography(
    displaySmall = TextStyle(
        fontFamily = YsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = YsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = YsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp
    ),
    titleMedium = TextStyle(
        fontFamily = YsFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    bodySmall = TextStyle(
        fontFamily = YsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = YsFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = YsFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 19.sp
    )
)