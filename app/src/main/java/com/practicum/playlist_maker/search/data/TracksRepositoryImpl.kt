package com.practicum.playlist_maker.search.data

import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.search.data.dto.TracksSearchRequest
import com.practicum.playlist_maker.search.data.dto.TracksSearchResponse
import com.practicum.playlist_maker.search.data.sharedPreferences.LocalStorage
import com.practicum.playlist_maker.search.domain.api.TracksRepository
import com.practicum.playlist_maker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            -1 -> emit(Resource.InternetError())
            200 -> emit(Resource.Success((response as TracksSearchResponse).results))
            else -> emit(Resource.ServerError())
        }
    }

    override suspend fun getTracksFromSearchHistory(): Flow<ArrayList<Track>> = flow {
        emit(localStorage.getTracks().toCollection(ArrayList()))
    }

    override fun addTrackToSearchHistory(track: Track) {
        localStorage.addTrack(track)
    }

    override fun clearSearchHistory() {
        localStorage.clearHistory()
    }
}