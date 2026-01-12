package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val getSearchHistoryInteractor: GetSearchHistoryInteractor,
    private val addTrackToHistoryInteractor: AddTrackToHistoryInteractor,
    private val clearSearchHistoryInteractor: ClearSearchHistoryInteractor
) :
    ViewModel() {
    private val stateLiveData = MutableLiveData<SearchScreenState>()
    fun observeState(): LiveData<SearchScreenState> = stateLiveData
    private var currentSearchText = ""
    private var trackSearchDebounce =
        debounce<String>(
            SEARCH_DEBOUNCE_DELAY,
            viewModelScope,
            true
        ) { changedText -> searchRequest(changedText) }

    fun searchDebounce(changedText: String) {
        if (currentSearchText != changedText) {
            currentSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    private fun searchRequest(query: String) {
        if (query.isNotEmpty()) {
            renderState(SearchScreenState.Loading)

            viewModelScope.launch {
                tracksInteractor(query).collect { pair ->
                    processResult(pair.first, pair.second)
                }
            }
        }
    }

    fun onTextChanged(value: String) {
        currentSearchText = value
        searchDebounce(value)
    }

    fun getSearchHistory() {
        val historyTracks = getSearchHistoryInteractor()
        stateLiveData.postValue(SearchScreenState.History(historyTracks))
    }

    fun addTrackToHistory(track: Track) {
        addTrackToHistoryInteractor(track)
    }

    fun historyClear() {
        clearSearchHistoryInteractor()
    }

    private fun renderState(state: SearchScreenState) {
        stateLiveData.postValue(state)
    }

    private fun processResult(data: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (data != null) {
            tracks.addAll(data)
        }
        when {
            errorMessage != null -> {
                renderState(
                    SearchScreenState.Error
                )
            }

            tracks.isEmpty() -> {
                renderState(SearchScreenState.Empty)
            }

            else -> {
                renderState(SearchScreenState.Content(tracks))
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}