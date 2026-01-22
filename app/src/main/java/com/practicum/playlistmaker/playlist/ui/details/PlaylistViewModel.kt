package com.practicum.playlistmaker.playlist.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.sharing.domain.model.Share
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private var playlist: Playlist,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {
    private val _playlistTracks = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = _playlistTracks

    init {
        loadData()
    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(track, playlist)
            loadData()
        }
    }

    fun playlistShare(): Share {
        val title = ""
        val text = buildString {
            append("${playlist.name}\n")
            append("${playlist.description}\n")
            append("${playlist.trackCount} треков\n")
            _playlistTracks.value?.let { state ->
                if (state is PlaylistState.Content) {
                    state.playlistTracks.forEachIndexed  { index, track ->
                        append(track.trackName)
                    }
                }
            }
        }
        return Share(text, title)
    }

    private fun loadData() {
        viewModelScope.launch {
            val updatedPlaylist = playlistInteractor.getPlaylistById(playlist.id)
            playlist = updatedPlaylist
            val tracks = playlistInteractor.getPlaylistTracks(playlist.trackIds)
            val time = tracks.sumOf { it.trackTimeMillis } / 1000 / 60
            _playlistTracks.postValue(PlaylistState.Content(playlist, time, tracks))
        }
    }
}
