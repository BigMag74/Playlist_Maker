package com.practicum.playlist_maker.playlist.ui

import com.practicum.playlist_maker.player.domain.model.Track


sealed class PlaylistScreenTracksState {

    class BasedState(val tracks: List<Track>) : PlaylistScreenTracksState()
}