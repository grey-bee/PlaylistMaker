package com.practicum.playlistmaker.util

interface ResourceProvider {
    fun getQuantityString(pluralRes: Int, quantity: Int): String
}