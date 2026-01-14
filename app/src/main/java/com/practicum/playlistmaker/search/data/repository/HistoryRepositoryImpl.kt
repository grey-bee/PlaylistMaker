package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.practicum.playlistmaker.favorites.data.db.AppDatabase
import com.practicum.playlistmaker.search.domain.api.HistoryRepository
import com.practicum.playlistmaker.search.domain.model.Track

class HistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val appDatabase: AppDatabase,
    private val gson: Gson
) :
    HistoryRepository {
    override suspend fun getHistory(): List<Track> {
        val rawList = sharedPreferences.getString(KEY_TRACKS, null) ?: return emptyList()
        val array = gson.fromJson(rawList, Array<Track>::class.java)
        val result = array?.toList() ?: emptyList()
        val favoriteTracksId = appDatabase.trackDao().getTracksId()
        val updateResult = result.map { track ->
            if (favoriteTracksId.contains(track.trackId)) track.copy(isFavorite = true)
            else track
        }
        return updateResult
    }

    override suspend fun addToHistory(track: Track) {
        val list = getHistory().toMutableList()
        list.removeIf { it.trackId == track.trackId }
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