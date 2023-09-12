package com.practicum.playlist_maker.playlist.domain.api

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistScreenInteractor {

    fun getPlaylistTracks(trackIds: List<Int>): Flow<List<Track>>
    suspend fun deleteTrackFromPlaylist(trackId: Int, playlist: Playlist)
}