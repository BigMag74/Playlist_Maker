package com.practicum.playlist_maker.playlist.ui

import com.practicum.playlist_maker.player.domain.model.Track


sealed class PlaylistScreenTracksState {

    class ContentState(val tracks: List<Track>) : PlaylistScreenTracksState()
    class EmptyState() : PlaylistScreenTracksState()
}