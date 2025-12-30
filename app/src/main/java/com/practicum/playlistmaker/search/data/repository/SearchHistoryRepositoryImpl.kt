package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.model.Track

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences, private val gson: Gson
) :
    SearchHistoryRepository {
    override fun getHistory(): List<Track> {
        val rawList = sharedPreferences.getString(KEY_TRACKS, null) ?: return emptyList()
        val array = gson.fromJson(rawList, Array<Track>::class.java)
        val result = array?.toList() ?: emptyList()
        return result
    }

    override fun addToHistory(track: Track) {
        val list = getHistory().toMutableList()
        list.remove(track)
        list.add(0, track)
        list.size.let { if (it > 10) list.removeAt(list.lastIndex) }
        saveHistory(list)
    }

    override fun clearHistory() {
        sharedPreferences.edit { clear() }
    }

    private fun saveHistory(tracks: List<Track>) {
        val rawList = gson.toJson(tracks)
        sharedPreferences.edit { putString(KEY_TRACKS, rawList) }
    }

    companion object {
        private const val KEY_TRACKS = "tracks"
    }
}