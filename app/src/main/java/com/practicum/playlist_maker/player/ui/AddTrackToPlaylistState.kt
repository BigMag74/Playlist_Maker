package com.practicum.playlist_maker.player.ui

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

sealed class AddTrackToPlaylistState {
    data class AlreadyAdded(val playlist: Playlist) : AddTrackToPlaylistState()
    class AddedNow(val playlist: Playlist) : AddTrackToPlaylistState()
}