package com.practicum.playlist_maker.creationPlaylist.domain.impl

import com.practicum.playlist_maker.creationPlaylist.domain.api.CreationPlaylistInteractor
import com.practicum.playlist_maker.creationPlaylist.domain.api.CreationPlaylistRepository
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

class CreationPlaylistInteractorImpl(private val creationPlaylistRepository: CreationPlaylistRepository) :
    CreationPlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        creationPlaylistRepository.addPlaylist(playlist)
    }

    override suspend fun getPlaylistById(id: Int): Playlist {
        return creationPlaylistRepository.getPlaylistById(id)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        creationPlaylistRepository.updatePlaylist(playlist)
    }


}