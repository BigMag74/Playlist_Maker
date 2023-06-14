package com.practicum.playlist_maker.search.data

import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.search.data.dto.TracksSearchRequest
import com.practicum.playlist_maker.search.data.dto.TracksSearchResponse
import com.practicum.playlist_maker.search.data.sharedPreferences.LocalStorage
import com.practicum.playlist_maker.search.domain.api.TracksRepository
import com.practicum.playlist_maker.utils.Resource

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> Resource.InternetError()
            200 -> {
                Resource.Success((response as TracksSearchResponse).results)
            }
            else -> Resource.Error()
        }

    }

    override fun getTracksFromSearchHistory(): ArrayList<Track> {
        return localStorage.getTracks().toCollection(ArrayList())
    }

    override fun addTrackToSearchHistory(track: Track) {
        localStorage.addTrack(track)
    }

    override fun clearSearchHistory() {
        localStorage.clearHistory()
    }
}