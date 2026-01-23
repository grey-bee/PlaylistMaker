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
    private val playlistInteractor: PlaylistInteractor,
    private var playlist: Playlist?
) : ViewModel() {
    private val _playlistSaved = MutableLiveData<Boolean>()
    fun observePlaylistSaved(): LiveData<Boolean> = _playlistSaved

    fun saveNewPlaylistInfo(title: String, description: String, uri: Uri?) {
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

    fun saveEditPlaylistInfo(title: String, description: String, uri: Uri?) {
        playlist?.let {
            viewModelScope.launch {
                val coverPath = uri?.let { playlistInteractor.saveImageToPrivateStorage(uri) }
                val newImagePath = coverPath ?: it.imagePath
                playlistInteractor.updatePlaylist(
                    it.copy(name = title, description = description, imagePath = newImagePath)
                )
                _playlistSaved.postValue(true)
            }
        }

    }
}