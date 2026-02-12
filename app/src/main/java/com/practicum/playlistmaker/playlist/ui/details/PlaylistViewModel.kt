package com.practicum.playlistmaker.playlist.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.toTimeString
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private var playlist: Playlist,
    private val playlistInteractor: PlaylistInteractor, application: Application
) : AndroidViewModel(application) {
    private val _playlistTracks = MutableLiveData<PlaylistState>()
    fun observeState(): LiveData<PlaylistState> = _playlistTracks
    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(track, playlist)
            loadData()
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
        }
    }

    fun getTracksString(): String {
        val state = observeState().value
        val tracksInfo = if (state is PlaylistState.Content) state.playlistTracks else null
        return buildString {
            tracksInfo?.forEachIndexed { index, track ->
                append("${index + 1}. ${track.artistName} - ${track.trackName} - ${track.trackTimeMillis.toTimeString()}\n")
            }
        }
    }

    fun getShareText(): String {
        return buildString {
            append("${playlist.name}\n")
            append("${playlist.description}\n")
            append(
                application.resources.getQuantityString(
                    R.plurals.tracks_count,
                    playlist.trackCount,
                    playlist.trackCount
                )
            )
            append("\n")
            append(getTracksString())
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val updatedPlaylist = playlistInteractor.getPlaylistById(playlist.id)
            playlist = updatedPlaylist
            val tracks = playlistInteractor.getPlaylistTracks(playlist.trackIds)
            val time = tracks.sumOf { it.trackTimeMillis } / 1000 / 60
            _playlistTracks.postValue(PlaylistState.Content(playlist, time, tracks))
        }
    }
}
