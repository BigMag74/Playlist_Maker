package com.practicum.playlist_maker.creationPlaylist.data.impl

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker.creationPlaylist.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.creationPlaylist.domain.db.CreationPlaylistRepository
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.player.data.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.reflect.Type


class CreationPlaylistRepositoryImpl(private val appDatabase: AppDatabase) :
    CreationPlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(convertFromPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(convertFromPlaylistToPlaylistEntity(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        emit(convertFromPlaylistEntitiesToPlaylists(appDatabase.playlistDao().getPlaylists()))
    }

    private fun convertFromPlaylistToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = 0,
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
}