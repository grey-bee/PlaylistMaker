package com.practicum.playlistmaker.playlist.ui.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private var coverPath: String? = null

    fun saveCoverAlbum(uri: Uri) {
        viewModelScope.launch {
            coverPath = playlistInteractor.saveImageToPrivateStorage(uri)
        }
    }

    fun savePlaylistInfo(title: String, description: String) {
        viewModelScope.launch {
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
        }
    }


}