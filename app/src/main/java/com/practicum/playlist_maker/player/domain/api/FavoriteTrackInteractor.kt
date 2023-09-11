package com.practicum.playlist_maker.player.domain.api

import com.practicum.playlist_maker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    suspend fun addTrackToFavorite(track: Track)
    suspend fun deleteTrackFromFavorite(track: Track)
    fun getFavoriteTracks(): Flow<List<Track>>
    fun getFavoriteTrackIds(): Flow<List<Int>>
}