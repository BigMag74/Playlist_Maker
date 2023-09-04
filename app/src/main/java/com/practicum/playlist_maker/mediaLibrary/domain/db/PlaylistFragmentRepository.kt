package com.practicum.playlist_maker.mediaLibrary.domain.db

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistFragmentRepository {

    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
}