package com.practicum.playlist_maker.search.domain.api

import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>

    suspend fun getTracksFromSearchHistory(): Flow<ArrayList<Track>>
    fun addTrackToSearchHistory(track: Track)
    fun clearSearchHistory()

}