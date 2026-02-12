package com.practicum.playlistmaker.playlist.ui.create

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.launch
import com.practicum.playlistmaker.util.Event

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {

    private val _playlistSaved = MutableLiveData<Event<Boolean>>()
    fun observePlaylistSaved(): LiveData<Event<Boolean>> = _playlistSaved

    fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            val imagePath = if (playlist.imagePath?.startsWith("content://") == true) {
                playlistInteractor.saveImageToPrivateStorage(playlist.imagePath.toUri())
            } else playlist.imagePath
            val editedPlaylist = playlist.copy(imagePath = imagePath)

            if (editedPlaylist.id.toInt() == 0) {
                playlistInteractor.addPlaylist(editedPlaylist)
            } else {
                playlistInteractor.updatePlaylist(editedPlaylist)
            }
            _playlistSaved.postValue(Event(true))
        }
    }
}