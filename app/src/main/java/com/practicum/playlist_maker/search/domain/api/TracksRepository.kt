package com.practicum.playlist_maker.search.domain.api

import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.utils.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
    fun getTracksFromSearchHistory(): ArrayList<Track>
    fun addTrackToSearchHistory(track: Track)
    fun clearSearchHistory()

}