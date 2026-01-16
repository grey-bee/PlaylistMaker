package com.practicum.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
//    private val _playlists = MutableLiveData<PlaylistState>()
//    fun observeState(): LiveData<PlaylistState> = _playlists
//
//    init {
//        playlistsRequest()
//    }
//
//    private fun playlistsRequest() {
//        viewModelScope.launch {
//            playlistInteractor.get().collect { playlists ->
//                if (playlists.isEmpty()) _playlists.postValue(PlaylistState.Empty)
//                else _playlists.postValue(PlaylistState.Content(playlists))
//            }
//        }
//    }
}