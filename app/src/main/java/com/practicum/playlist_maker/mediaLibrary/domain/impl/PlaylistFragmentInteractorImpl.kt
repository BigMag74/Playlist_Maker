package com.practicum.playlist_maker.mediaLibrary.domain.impl

import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.mediaLibrary.domain.api.PlaylistFragmentInteractor
import com.practicum.playlist_maker.mediaLibrary.domain.api.PlaylistFragmentRepository
import com.practicum.playlist_maker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistFragmentInteractorImpl(private val playlistFragmentRepository: PlaylistFragmentRepository) :
    PlaylistFragmentInteractor {

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistFragmentRepository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistFragmentRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        playlistFragmentRepository.addTrackToPlaylist(track, playlist)
    }

}