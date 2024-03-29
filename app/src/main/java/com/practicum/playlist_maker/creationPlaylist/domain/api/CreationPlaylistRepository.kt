package com.practicum.playlist_maker.creationPlaylist.domain.api

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

interface CreationPlaylistRepository {

    suspend fun addPlaylist(playlist: Playlist)
}