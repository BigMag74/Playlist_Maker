package com.practicum.playlist_maker.creationPlaylist.domain.impl

import com.practicum.playlist_maker.creationPlaylist.domain.db.CreationPlaylistInteractor
import com.practicum.playlist_maker.creationPlaylist.domain.db.CreationPlaylistRepository
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

class CreationPlaylistInteractorImpl(private val creationPlaylistRepository: CreationPlaylistRepository) :
    CreationPlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist) {
        creationPlaylistRepository.addPlaylist(playlist)
    }


}