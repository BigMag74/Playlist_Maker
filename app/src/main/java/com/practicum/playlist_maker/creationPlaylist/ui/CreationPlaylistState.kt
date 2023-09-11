package com.practicum.playlist_maker.creationPlaylist.ui

sealed class CreationPlaylistState() {
    class EmptyName() : CreationPlaylistState()
    class ContentName() : CreationPlaylistState()
}
