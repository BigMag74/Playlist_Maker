package com.practicum.playlist_maker.search.domain.api

import com.practicum.playlist_maker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

    suspend fun getTracksFromSearchHistory(): Flow<ArrayList<Track>>
    fun addTrackToSearchHistory(track: Track)
    fun clearSearchHistory()

}