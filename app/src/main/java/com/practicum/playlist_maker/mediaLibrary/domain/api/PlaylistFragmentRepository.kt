package com.practicum.playlist_maker.mediaLibrary.domain.api

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistFragmentRepository {

    suspend fun deletePlaylist(playlist: Playlist)
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist)
}