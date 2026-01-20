package com.practicum.playlistmaker.playlist.ui.create

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val _playlistSaved = MutableLiveData<Boolean>()
    fun observePlaylistSaved(): LiveData<Boolean> = _playlistSaved

    fun savePlaylistInfo(title: String, description: String, uri: Uri?) {
        viewModelScope.launch {
            val coverPath = uri?.let { playlistInteractor.saveImageToPrivateStorage(uri) }
            playlistInteractor.addPlaylist(
                Playlist(
                    0,
                    title,
                    description,
                    coverPath,
                    emptyList(),
                    0
                )
            )
            _playlistSaved.postValue(true)
        }
    }
}