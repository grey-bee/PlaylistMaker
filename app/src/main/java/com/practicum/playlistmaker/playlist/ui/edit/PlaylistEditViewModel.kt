package com.practicum.playlistmaker.playlist.ui.edit

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    private var playlist: Playlist,
    private val playlistInteractor: PlaylistInteractor

) : ViewModel() {
    private val _playlistSaved = MutableLiveData<Boolean>()
    fun observePlaylistSaved(): LiveData<Boolean> = _playlistSaved
    fun savePlaylistInfo(title: String, description: String, uri: Uri?) {
        viewModelScope.launch {
            val coverPath = uri?.let { playlistInteractor.saveImageToPrivateStorage(uri) }
            val newImagePath = coverPath ?: playlist.imagePath
            playlistInteractor.updatePlaylist(
                playlist.copy(name = title, description = description, imagePath = newImagePath)
            )
            _playlistSaved.postValue(true)
        }
    }

}