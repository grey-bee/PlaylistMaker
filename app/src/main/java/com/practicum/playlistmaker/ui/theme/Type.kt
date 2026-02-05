package com.practicum.playlistmaker.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.practicum.playlistmaker.R
import  androidx.compose.ui.text.TextStyle
import  androidx.compose.material3.Typography
import androidx.compose.ui.unit.sp


val YsFamily = FontFamily(
    Font(R.font.ys_display_regular, FontWeight.Normal),
    Font(R.font.ys_display_medium, FontWeight.Medium),
    Font(R.font.ys_display_bold, FontWeight.Bold)
)

val PlaylistTypography = Typography(
    labelMedium = TextStyle(
        fontFamily = YsFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = YsFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    )
)