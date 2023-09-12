package com.practicum.playlist_maker.playlist.data.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker.creationPlaylist.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.player.data.db.AppDatabase
import com.practicum.playlist_maker.player.data.db.entity.TrackInPlaylistsEntity
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.playlist.domain.api.PlaylistScreenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type

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

    override suspend fun deleteTrackFromPlaylist(trackId: Int, playlist: Playlist) {
        playlist.trackIds.remove(trackId)
        appDatabase.playlistDao()
            .updatePlaylist(convertFromPlaylistToPlaylistEntity(playlist.copy(countOfTracks = playlist.countOfTracks - 1)))
        deleteTrackWithoutPlaylist(trackId)
    }

    private suspend fun deleteTrackWithoutPlaylist(trackId: Int) {
        val type: Type = object : TypeToken<List<Int?>?>() {}.type
        val playlists = appDatabase.playlistDao().getPlaylists()
        var trackIds: List<Int>? = null

        for (playlistEntity: PlaylistEntity in playlists) {
            trackIds = Gson().fromJson(
                playlistEntity.trackIds,
                type
            )
            if (trackIds?.contains(trackId) == true) {
                appDatabase.trackInPlaylistDao().deleteTrackById(trackId)
                break
            }
        }

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

    private fun convertFromPlaylistToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            path = playlist.pathUri.toString(),
            trackIds = Gson().toJson(playlist.trackIds),
            countOfTracks = playlist.countOfTracks,
        )
    }


}