package com.practicum.playlistmaker.medialibrary.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.model.Track

class FeaturedTracksViewModel: ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>()
    fun observeState(): LiveData<List<Track>> = _tracks

    init {
        _tracks.postValue(emptyList<Track>())
    }
}