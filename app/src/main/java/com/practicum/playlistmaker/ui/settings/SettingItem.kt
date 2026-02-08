package com.practicum.playlistmaker.ui.settings

data class SettingItem(
    val nameItem: Int,
    val type: SettingType,
    val onClick: () -> Unit = {}
)