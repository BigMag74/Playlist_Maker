package com.practicum.playlist_maker.mediaLibrary.data.impl

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker.creationPlaylist.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.mediaLibrary.domain.api.PlaylistFragmentRepository
import com.practicum.playlist_maker.player.data.db.AppDatabase
import com.practicum.playlist_maker.player.data.db.entity.TrackInPlaylistsEntity
import com.practicum.playlist_maker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type
import java.util.*

class PlaylistFragmentRepositoryImpl(private val appDatabase: AppDatabase) :
    PlaylistFragmentRepository {

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(convertFromPlaylistToPlaylistEntity(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        emit(convertFromPlaylistEntitiesToPlaylists(appDatabase.playlistDao().getPlaylists()))
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlist.trackIds.add(track.trackId)
        appDatabase.playlistDao()
            .updatePlaylist(convertFromPlaylistToPlaylistEntity(playlist.copy(countOfTracks = playlist.countOfTracks + 1)))

        appDatabase.trackInPlaylistDao()
            .insertTrack(convertFromTrackToTrackInPlaylistsEntity(track))
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

    private fun convertFromPlaylistEntitiesToPlaylists(playlistEntities: List<PlaylistEntity>): List<Playlist> {
        val type: Type = object : TypeToken<List<Int?>?>() {}.type
        return playlistEntities.map { playlistEntity ->
            Playlist(
                id = playlistEntity.id,
                name = playlistEntity.name,
                description = playlistEntity.description,
                pathUri = Uri.parse(playlistEntity.path),
                trackIds = Gson().fromJson(
                    playlistEntity.trackIds,
                    type
                ),
                countOfTracks = playlistEntity.countOfTracks
            )
        }
    }

    private fun convertFromTrackToTrackInPlaylistsEntity(track: Track): TrackInPlaylistsEntity {
        return TrackInPlaylistsEntity(
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
}