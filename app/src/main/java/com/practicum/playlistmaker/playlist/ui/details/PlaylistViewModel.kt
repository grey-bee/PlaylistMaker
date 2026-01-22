package com.practicum.playlistmaker.playlist.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.Playlist
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.sharing.domain.model.Share
import com.practicum.playlistmaker.util.ResourceProvider
import com.practicum.playlistmaker.util.toTimeString
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private var playlist: Playlist,
    private val playlistInteractor: PlaylistInteractor,
    private val resourceProvider: ResourceProvider
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

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(playlist)
        }
    }

    fun playlistShare(): Share {
        val title = ""
        val text = buildString {
            append("${playlist.name}\n")
            append("${playlist.description}\n")
            append(
                resourceProvider.getQuantityString(
                    R.plurals.tracks_count,
                    playlist.trackCount
                )
            )
            append("\n")
            _playlistTracks.value?.let { state ->
                if (state is PlaylistState.Content) {
                    state.playlistTracks.forEachIndexed { index, track ->
                        append("${index + 1}. ${track.artistName} - ${track.trackName} - ${track.trackTimeMillis.toTimeString()}\n")
                    }
                }
            }
        }
        return Share(text, title)
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
