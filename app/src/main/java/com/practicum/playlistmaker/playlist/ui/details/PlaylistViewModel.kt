package com.practicum.playlistmaker.playlist.ui.details

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.model.Playlist

class PlaylistViewModel(
    private val playlist: Playlist,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
//    private val _playlistTracks = MutableLiveData<PlaylistsState>()
//    fun observeState(): LiveData<PlaylistsState> = _playlists
}

//нужно написать State
//1. Создать LiveData для данных (треки, общее время)
//2. Загрузить треки — у тебя же есть playlistInteractor.getPlaylistTracks(trackIds)
//3. Подсчитать общее время — tracks.sumOf { it.trackTimeMillis }
//
//Где вызвать загрузку? Варианты:
//- В init { } блоке — сразу при создании ViewModel
//- Или отдельный метод который Fragment вызовет
//
//Как в других ViewModel проекта делается начальная загрузка данных? Посмотри например SearchViewModel или FavoritesViewModel — там наверняка есть init или подобное.