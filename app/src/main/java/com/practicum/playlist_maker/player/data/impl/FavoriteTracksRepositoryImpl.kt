package com.practicum.playlist_maker.player.data.impl

import com.practicum.playlist_maker.player.data.db.AppDatabase
import com.practicum.playlist_maker.player.data.db.entity.FavoriteTrackEntity
import com.practicum.playlist_maker.player.domain.db.FavoriteTracksRepository
import com.practicum.playlist_maker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*


class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
) : FavoriteTracksRepository {
    override suspend fun addTrackToFavorite(track: Track) {
        appDatabase.favoriteTrackDao().insertTrack(convertFromTrackToTrackEntity(track))
    }

    override suspend fun deleteTrackFromFavorite(track: Track) {
        appDatabase.favoriteTrackDao().deleteTrack(convertFromTrackToTrackEntity(track))
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val favoriteTracks = appDatabase.favoriteTrackDao().getTracks()
        emit(convertFromTrackEntitiesToTracks(favoriteTracks))
    }

    override fun getFavoriteTrackIds(): Flow<List<Int>> = flow {
        val favoriteTrackIds = appDatabase.favoriteTrackDao().getTracksId()
        emit(favoriteTrackIds)
    }

    private fun convertFromTrackToTrackEntity(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            created_at = Date().time,
        )

    }

    private fun convertFromTrackEntitiesToTracks(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track ->
            Track(
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.trackId,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl,
            )
        }
    }
}