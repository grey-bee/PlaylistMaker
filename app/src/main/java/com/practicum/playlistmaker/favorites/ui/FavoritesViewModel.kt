package com.practicum.playlistmaker.favorites.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorites.domain.FavoritesInteractor
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {
    private val _tracks = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = _tracks

    init {
        favoriteRequest()
    }

    private fun favoriteRequest() {
        viewModelScope.launch {
            favoritesInteractor.getTracks().collect { tracks ->
                if (tracks.isEmpty()) _tracks.postValue(FavoritesState.Empty)
                else _tracks.postValue(FavoritesState.Content(tracks))
            }
        }
    }
}