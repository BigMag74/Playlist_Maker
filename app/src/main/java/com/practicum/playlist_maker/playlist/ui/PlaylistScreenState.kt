package com.practicum.playlist_maker.playlist.ui

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

sealed class PlaylistScreenState {
    class BasedState(val playlist: Playlist) : PlaylistScreenState()
}
