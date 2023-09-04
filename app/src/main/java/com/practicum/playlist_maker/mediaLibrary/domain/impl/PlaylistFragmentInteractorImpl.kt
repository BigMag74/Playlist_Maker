package com.practicum.playlist_maker.mediaLibrary.domain.impl

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.mediaLibrary.domain.db.PlaylistFragmentInteractor
import com.practicum.playlist_maker.mediaLibrary.domain.db.PlaylistFragmentRepository
import kotlinx.coroutines.flow.Flow

class PlaylistFragmentInteractorImpl(private val playlistFragmentRepository: PlaylistFragmentRepository) :
    PlaylistFragmentInteractor {

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistFragmentRepository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistFragmentRepository.getPlaylists()
    }
}