package com.practicum.playlist_maker.creationPlaylist.data.impl

import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlist_maker.creationPlaylist.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.creationPlaylist.domain.api.CreationPlaylistRepository
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.player.data.db.AppDatabase
import java.lang.reflect.Type


class CreationPlaylistRepositoryImpl(private val appDatabase: AppDatabase) :
    CreationPlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(convertFromPlaylistToPlaylistEntity(playlist))
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        return convertFromPlaylistEntityToPlaylist(appDatabase.playlistDao().getPlaylistById(id))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(convertFromPlaylistToPlaylistEntity(playlist))
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

    private fun convertFromPlaylistEntityToPlaylist(playlistEntity: PlaylistEntity): Playlist {
        val type: Type = object : TypeToken<MutableList<Int?>?>() {}.type
        return Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            pathUri = playlistEntity.path,
            trackIds = Gson().fromJson(playlistEntity.trackIds, type),
            countOfTracks = playlistEntity.countOfTracks,
        )
    }


}