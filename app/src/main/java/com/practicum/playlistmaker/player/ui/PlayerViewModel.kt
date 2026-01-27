package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorites.domain.FavoritesInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.list.PlaylistsState
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.Event
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private val _playlists = MutableLiveData<PlaylistsState>()
    fun observePlaylists(): LiveData<PlaylistsState> = _playlists
    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerScreenState(): LiveData<PlayerState> = playerState
    private var playerControl: PlayerControl? = null
    private var isScreenOpened = false

    fun setPlayerControl(playerControl: PlayerControl) {
        this.playerControl = playerControl
        viewModelScope.launch {
            playerControl.getPlayerState().collect {
                playerState.postValue(it)
                if (!isScreenOpened && it !is PlayerState.Playing) {
                    playerControl.stopForeground()
                }
            }
        }
    }

    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData
    private val toastMessage = MutableLiveData<Event<String>>()
    fun observeToastMessage(): LiveData<Event<String>> = toastMessage

    init {
        playlistsRequest()
        viewModelScope.launch {
            isFavoriteLiveData.postValue(favoritesInteractor.isFavorite(track.trackId))
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerControl = null
    }

    fun playbackControl() {
        when (playerState.value) {
            is PlayerState.Playing -> playerControl?.pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> playerControl?.startPlayer()
            else -> {}
        }
    }

    fun onFavoriteClicked(track: Track) {
        val check = isFavoriteLiveData.value ?: track.isFavorite
        if (check) {
            viewModelScope.launch { favoritesInteractor.deleteTrack(track.trackId) }
        } else {
            viewModelScope.launch { favoritesInteractor.addTrack(track) }
        }
        isFavoriteLiveData.postValue(!check)
    }

    fun onAddToPlaylistClicked(playlist: Playlist): Boolean {
        if (playlist.trackIds.contains(track.trackId)) {
            toastMessage.postValue(Event("Трек уже добавлен в плейлист ${playlist.name}"))
            return false
        } else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(track, playlist)
            }
            toastMessage.postValue(Event("Добавлено в плейлист ${playlist.name}"))
            return true
        }
    }

    fun removePlayerControl() {
        playerControl = null
    }

    fun screenOpen() {
        playerControl?.stopForeground()
        isScreenOpened = true
    }

    fun screenClose() {
        when (playerState.value) {
            is PlayerState.Playing -> {
                playerControl?.startForeground()
            }

            else -> {}
        }
        isScreenOpened = false
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