package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.playlist.domian.model.Playlist

class PlaylistsViewModel: ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>()
    fun observeState(): LiveData<List<Playlist>> = _playlists

    init {
        _playlists.postValue(emptyList<Playlist>())
    }

}