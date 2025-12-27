package com.practicum.playlistmaker.search.ui


import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.AddTrackToHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.ClearSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.GetSearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.model.Track

class SearchViewModel(
    private val searchTracksInteractor: SearchTracksInteractor,
    private val getSearchHistoryInteractor: GetSearchHistoryInteractor,
    private val addTrackToHistoryInteractor: AddTrackToHistoryInteractor,
    private val clearSearchHistoryInteractor: ClearSearchHistoryInteractor
) :
    ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searching() }
    private val stateLiveData = MutableLiveData<SearchScreenState>()
    fun observeState(): LiveData<SearchScreenState> = stateLiveData
    private var currentSearchText = ""
    private var isClickAllowed = true

    fun onTextChanged(value: String) {
        currentSearchText = value
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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

    fun openPlayer() {
    }

    private fun searching() {
        stateLiveData.postValue(SearchScreenState.Loading)
        searchTracksInteractor(currentSearchText, object : SearchTracksInteractor.Consumer {
            override fun consume(foundTracks: List<Track>?) {
                if (foundTracks != null) {
                    if (foundTracks.isNotEmpty()) {
                        stateLiveData.postValue(SearchScreenState.Content(foundTracks))
                    } else {
                        stateLiveData.postValue(SearchScreenState.Empty)
                    }
                } else {
                    stateLiveData.postValue(SearchScreenState.Error)
                }
            }
        })
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }


    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        fun getFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.provideSearchTracksInteractor(),
                    Creator.provideGetSearchHistoryInteractor(context),
                    Creator.provideAddTrackToHistoryInteractor(context),
                    Creator.provideClearSearchHistoryInteractor(context)
                )
            }

        }
    }
}