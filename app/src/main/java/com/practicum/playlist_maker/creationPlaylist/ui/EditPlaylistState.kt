package com.practicum.playlist_maker.creationPlaylist.ui

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

sealed class EditPlaylistState() {
    class EmptyName() : EditPlaylistState()
    class ContentName() : EditPlaylistState()
    class JustOpened(val playlist: Playlist) : EditPlaylistState()
}
