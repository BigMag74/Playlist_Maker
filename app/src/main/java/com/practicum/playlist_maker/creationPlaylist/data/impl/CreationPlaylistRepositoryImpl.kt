package com.practicum.playlist_maker.creationPlaylist.data.impl

import com.google.gson.Gson
import com.practicum.playlist_maker.creationPlaylist.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.creationPlaylist.domain.db.CreationPlaylistRepository
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.player.data.db.AppDatabase


class CreationPlaylistRepositoryImpl(private val appDatabase: AppDatabase) :
    CreationPlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(convertFromPlaylistToPlaylistEntity(playlist))
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


}