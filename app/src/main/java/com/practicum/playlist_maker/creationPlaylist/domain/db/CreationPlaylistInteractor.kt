package com.practicum.playlist_maker.creationPlaylist.domain.db

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface CreationPlaylistInteractor {

    suspend fun addPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
}