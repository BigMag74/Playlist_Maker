package com.practicum.playlist_maker.search.ui

import com.practicum.playlist_maker.player.domain.model.Track

sealed interface SearchState {
    object Loading : SearchState

    data class Content(
        val tracks: List<Track>
    ) : SearchState

    data class Error(
        val errorMessageResId: Int
    ) : SearchState

    data class Empty(
        val emptyMessageResId: Int
    ) : SearchState

    data class SearchHistory(
        val tracks: List<Track>?
    ) : SearchState
}