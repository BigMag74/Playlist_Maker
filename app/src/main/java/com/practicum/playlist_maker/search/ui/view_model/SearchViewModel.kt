package com.practicum.playlist_maker.search.ui.view_model

import androidx.lifecycle.*
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.ui.SearchState
import com.practicum.playlist_maker.utils.debounce
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {


    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData


    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    private var latestSearchText: String? = null

    private val tracksSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            searchRequest(changedText)
        }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            tracksSearchDebounce(changedText)
        }
    }


    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchState.Loading)

            viewModelScope.launch {
                tracksInteractor.searchTracks(newSearchText).collect { pair ->
                    processResult(pair.first, pair.second)
                }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, message: String?) {
        val tracks = mutableListOf<Track>()

        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            message == INTERNET_ERROR -> {
                latestSearchText = ""
                renderState(
                    SearchState.Error(
                        R.string.internet_issues
                    )
                )
            }

            message == SERVER_ERROR -> {
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


    fun addTrackToSearchHistory(track: Track) {
        tracksInteractor.addTrackToSearchHistory(track)
    }

    fun getTracksFromSearchHistory(): ArrayList<Track> {
        return tracksInteractor.getTracksFromSearchHistory()
    }

    fun clearSearchHistory() {
        tracksInteractor.clearSearchHistory()
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SERVER_ERROR = "Server error"
        private const val INTERNET_ERROR = "Internet error"
    }
}