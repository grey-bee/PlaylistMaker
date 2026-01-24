package com.practicum.playlistmaker.player.ui

import android.media.MediaPlayer
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val track: Track,
    private val mediaPlayer: MediaPlayer,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val _playlists = MutableLiveData<PlaylistsState>()
    fun observePlaylists(): LiveData<PlaylistsState> = _playlists

    private var timerJob: Job? = null
    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerScreenState(): LiveData<PlayerState> = playerState
    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeIsFavorite(): LiveData<Boolean> = isFavoriteLiveData
    private val toastMessage = MutableLiveData<Event<String>>()
    fun observeToastMessage(): LiveData<Event<String>> = toastMessage

    init {
        initMediaPlayer()
        playlistsRequest()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    private fun initMediaPlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared())
        }
        mediaPlayer.setOnCompletionListener {
            timerJob?.cancel()
            playerState.postValue(PlayerState.Prepared())
        }
        viewModelScope.launch {
            isFavoriteLiveData.postValue(favoritesInteractor.isFavorite(track.trackId))
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
    }

    private fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState.value = PlayerState.Default()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (mediaPlayer.isPlaying) {
                delay(300L)
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    fun playbackControl() {
        when (playerState.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
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

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            ?: "00:00"
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