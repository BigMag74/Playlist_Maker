package com.practicum.playlist_maker.player.ui

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

sealed class AudioPlayerPlaylistsState {
    class EmptyPlaylists() : AudioPlayerPlaylistsState()
    data class ContentPlaylists(val playlists: List<Playlist>) : AudioPlayerPlaylistsState()
}
