package com.practicum.playlistmaker.search.ui


import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.api.SearchTracksInteractor
import com.practicum.playlistmaker.search.domain.model.Track

class SearchViewModel : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { searching() }
    private val stateLiveData = MutableLiveData<SearchScreenState>()
    fun observeState(): LiveData<SearchScreenState> = stateLiveData

    private var currentSearchText = ""
    private val searchTracksInteractor by lazy { Creator.provideSearchTracksInteractor() }

    fun onTextChanged(value: String) {
        currentSearchText = value
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
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

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}

//private fun clickDebounce(): Boolean {
//    val current = isClickAllowed
//    if (isClickAllowed) {
//        isClickAllowed = false
//        handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
//    }
//    return current
//}

//    private val getSearchHistoryInteractor by lazy { Creator.provideGetSearchHistoryInteractor(this) }
//    private val clearSearchHistoryInteractor by lazy {
//        Creator.provideClearSearchHistoryInteractor(
//            this
//        )
//    }

//    private val addTrackToHistoryInteractor by lazy {
//        Creator.provideAddTrackToHistoryInteractor(
//            this
//        )
//    }

//        searchTracksInteractor(
//            searchField.text.toString(),
//            object : SearchTracksInteractor.Consumer {
//                override fun consume(foundTracks: List<Track>?) {
//                    tracks.clear()
//                    progress.isVisible = false
//                    if (foundTracks != null) {
//                        if (foundTracks.isNotEmpty()) {
//                            showContent()
//                            tracks.addAll(foundTracks)
//                            trackAdapter.notifyDataSetChanged()
//                        } else {
//                            showNothingFound()
//                        }
//                    } else {
//                        showError()
//                    }
//                }
//            }
//        )

//    private val searchTracksInteractor by lazy { Creator.provideSearchTracksInteractor() }

//private fun updateHistory() {
////        val historySource = getSearchHistoryInteractor()
//    historyTracks.clear()
////        if (historySource.isNotEmpty()) historyTracks.addAll(historySource)
////        historyAdapter.notifyDataSetChanged()
//}


//private fun clearHistory() {
////        clearSearchHistoryInteractor()
//    historyTracks.clear()
////        historyAdapter.notifyDataSetChanged()
//    binding.layoutHistory.isVisible = false
//}