package com.practicum.playlistmaker

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import androidx.core.content.edit

object SearchHistoryManager {
    private lateinit var sharedPrefs: SharedPreferences
    private val gson = Gson()

    fun initialize(context: Context) {
        sharedPrefs = context.getSharedPreferences("history", MODE_PRIVATE)
    }

    fun getTrackList(): List<Track> {
        val rawList = sharedPrefs.getString("tracks", null)
        Log.d("History", "Raw JSON: $rawList")
        val array = gson.fromJson(rawList, Array<Track>::class.java)
        val result = array?.toList() ?: emptyList()
        Log.d("History", "Loaded ${result.size} tracks")
        return result
    }

    private fun saveTrackList(list: MutableList<Track>) {
        val rawList = gson.toJson(list)
        sharedPrefs.edit { putString("tracks", rawList) }
    }

    fun addTrack(track: Track) {
        val list = getTrackList().toMutableList()
        list.remove(track)
        list.add(0, track)
        list.size.let { if (it > 10) list.removeAt(list.lastIndex) }
        saveTrackList(list)
    }

    fun clear() {
        sharedPrefs.edit { clear() }
    }
}