package com.practicum.playlistmaker.playlist.data

import android.content.Context
import com.practicum.playlistmaker.util.ResourceProvider

class ResourceProviderImpl(private val context: Context) : ResourceProvider {
    override fun getQuantityString(pluralRes: Int, quantity: Int): String {
        return context.resources.getQuantityString(pluralRes, quantity, quantity)
    }
}