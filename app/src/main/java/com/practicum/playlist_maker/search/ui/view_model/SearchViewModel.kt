package com.practicum.playlist_maker.search.ui.view_model

import androidx.lifecycle.*
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.ui.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {


    private val stateLiveData = MutableLiveData<SearchState>()
    fun observeState(): LiveData<SearchState> = stateLiveData

    init {
        showHistory()
    }

    private var searchJob: Job? = null

    private fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    private var latestSearchText: String? = null


    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText

            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                searchRequest(changedText)
            }
        }
        if (changedText.isEmpty()) {
            searchJob?.cancel()
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

    fun showHistory() {
        viewModelScope.launch {
            tracksInteractor.getTracksFromSearchHistory().collect { tracks ->
                if (tracks.isEmpty())
                    renderState(SearchState.SearchHistory(null))
                else
                    renderState(SearchState.SearchHistory(tracks))
            }
        }
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