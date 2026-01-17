package com.practicum.playlistmaker.playlist.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val _playlists = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = _playlists

    init {
        playlistsRequest()
    }

    private fun playlistsRequest() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect { playlists ->
                if (playlists.isEmpty()) _playlists.postValue(PlaylistsState.Empty)
                else _playlists.postValue(PlaylistsState.Content(playlists))
            }
        }
    }
}