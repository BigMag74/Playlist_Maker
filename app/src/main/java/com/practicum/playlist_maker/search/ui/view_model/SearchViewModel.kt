package com.practicum.playlist_maker.search.ui.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.ui.SearchState

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData


    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    private var latestSearchText: String? = null


    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )

    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            tracksInteractor.searchTracks(newSearchText, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, message: String?) {
                    val tracks = mutableListOf<Track>()
                    if (foundTracks != null) {
                        tracks.addAll(foundTracks)
                    }

                    when {
                        message == "Internet error" -> {
                            latestSearchText = ""
                            renderState(
                                SearchState.Error(
                                    R.string.internet_issues
                                )
                            )
                        }

                        message == "Server error" -> {
                            latestSearchText = ""
                            renderState(
                                SearchState.Error(
                                    R.string.server_issues
                                )
                            )
                        }

                        tracks.isEmpty() -> {
                            renderState(
                                SearchState.Empty(
                                    R.string.nothing_found
                                )
                            )
                        }

                        else -> {
                            renderState(
                                SearchState.Content(
                                    tracks,
                                )
                            )
                        }
                    }

                }
            })
        }
    }

    fun removeAllCallbacks() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun addTrackToSearchHistory(track: Track) {
        tracksInteractor.addTrackToSearchHistory(track)
    }

    fun getTracksFromSearchHistory(): ArrayList<Track> {
        return tracksInteractor.getTracksFromSearchHistory()
    }

    fun clearSearchHistory() {
        tracksInteractor.clearSearchHistory()
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()


        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = this[APPLICATION_KEY]?.let { Creator.provideTracksInteractor(it) }
                SearchViewModel(interactor!!)
            }
        }
    }
}