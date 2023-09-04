package com.practicum.playlist_maker.creationPlaylist.domain.impl

import com.practicum.playlist_maker.creationPlaylist.domain.db.CreationPlaylistInteractor
import com.practicum.playlist_maker.creationPlaylist.domain.db.CreationPlaylistRepository
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class CreationPlaylistInteractorImpl(private val creationPlaylistRepository: CreationPlaylistRepository) :
    CreationPlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        creationPlaylistRepository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        creationPlaylistRepository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return creationPlaylistRepository.getPlaylists()
    }
}