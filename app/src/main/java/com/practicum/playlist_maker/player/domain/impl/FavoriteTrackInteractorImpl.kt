package com.practicum.playlist_maker.player.domain.impl

import com.practicum.playlist_maker.player.domain.db.FavoriteTrackInteractor
import com.practicum.playlist_maker.player.domain.db.FavoriteTracksRepository
import com.practicum.playlist_maker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class FavoriteTrackInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTrackInteractor {

    override suspend fun addTrackToFavorite(track: Track) {
        favoriteTracksRepository.addTrackToFavorite(track)
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        favoriteTracksRepository.deleteTrackFromFavorite(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getFavoriteTracks()
    }

    override fun getFavoriteTrackIds(): Flow<List<Int>>{
        return favoriteTracksRepository.getFavoriteTrackIds()
    }
}