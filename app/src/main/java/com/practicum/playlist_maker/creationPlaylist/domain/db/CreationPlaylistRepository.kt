package com.practicum.playlist_maker.creationPlaylist.domain.db

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

interface CreationPlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist)
}