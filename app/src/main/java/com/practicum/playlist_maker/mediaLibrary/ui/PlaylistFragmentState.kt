package com.practicum.playlist_maker.mediaLibrary.ui

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

sealed class PlaylistFragmentState() {
    class EmptyPlaylists() : PlaylistFragmentState()
    data class ContentPlaylists(val playlists: List<Playlist>) : PlaylistFragmentState()
}
