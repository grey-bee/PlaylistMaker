package com.practicum.playlistmaker.ui.settings

sealed class SettingType {
    data class Switch(val checked: Boolean, val onCheckedChange: (Boolean) -> Unit) : SettingType()
    data class ItemIcon(val iconRes: Int) : SettingType()
}