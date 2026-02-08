package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.api.HistoryAddInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryClearInteractor
import com.practicum.playlistmaker.search.domain.api.HistoryGetInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.model.Track
import com.practicum.playlistmaker.util.debounce
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val historyGetInteractor: HistoryGetInteractor,
    private val historyAddInteractor: HistoryAddInteractor,
    private val historyClearInteractor: HistoryClearInteractor
) :
    ViewModel() {
    private val stateLiveData = MutableLiveData<SearchScreenState>()
    fun observeState(): LiveData<SearchScreenState> = stateLiveData
    var searchTextLiveData = MutableLiveData<String>()
    fun observeSearchText(): LiveData<String> = searchTextLiveData
    private var trackSearchDebounce =
        debounce<String>(
            SEARCH_DEBOUNCE_DELAY,
            viewModelScope,
            true
        ) { changedText -> searchRequest(changedText) }

    init {
        getSearchHistory()
    }

    fun searchRequest(query: String) {
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
        if (value.isEmpty()) getSearchHistory()
        if (searchTextLiveData.value != value) {
            searchTextLiveData.postValue(value)
            trackSearchDebounce(value)
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            val historyTracks = historyGetInteractor()
            stateLiveData.postValue(SearchScreenState.History(historyTracks))
        }

    }

    fun addTrackToHistory(track: Track) {
        viewModelScope.launch {
            historyAddInteractor(track)
        }
    }

    fun historyClear() {
        historyClearInteractor()
        getSearchHistory()
    }

    private fun renderState(state: SearchScreenState) {
        stateLiveData.postValue(state)
    }

    private fun processResult(data: List<Track>?, errorMessage: String?) {
        println("data: $data, error: $errorMessage")
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