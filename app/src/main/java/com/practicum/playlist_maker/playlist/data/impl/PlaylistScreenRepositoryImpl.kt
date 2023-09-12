package com.practicum.playlist_maker.playlist.data.impl

import com.practicum.playlist_maker.player.data.db.AppDatabase
import com.practicum.playlist_maker.player.data.db.entity.TrackInPlaylistsEntity
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.playlist.domain.api.PlaylistScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistScreenRepositoryImpl(private val appDatabase: AppDatabase) :
    PlaylistScreenRepository {

    override fun getPlaylistTracks(trackIds: List<Int>): Flow<List<Track>> = flow {
        val tracks = convertTrackInPlaylistEntitiesToTracks(
            appDatabase.trackInPlaylistDao().getAllTracks()
        )
        val res: MutableList<Track> = mutableListOf()
        for (track in tracks) {
            if (trackIds.contains(track.trackId))
                res.add(track)

        }
        emit(res)
    }

    private fun convertTrackInPlaylistEntitiesToTracks(tracks: List<TrackInPlaylistsEntity>): List<Track> {
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