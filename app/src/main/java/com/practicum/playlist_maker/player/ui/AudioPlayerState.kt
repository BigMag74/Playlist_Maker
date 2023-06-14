package com.practicum.playlist_maker.player.ui

import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.search.ui.SearchState

sealed interface AudioPlayerState {
    object STATE_DEFAULT : AudioPlayerState
    object STATE_PREPARED : AudioPlayerState
    data class STATE_PLAYING(
        val time: Int,
    ) : AudioPlayerState

    object STATE_PAUSED : AudioPlayerState
}
