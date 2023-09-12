package com.practicum.playlist_maker.playlist.domain.impl

import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.playlist.domain.api.PlaylistScreenInteractor
import com.practicum.playlist_maker.playlist.domain.api.PlaylistScreenRepository
import kotlinx.coroutines.flow.Flow

class PlaylistScreenInteractorImpl(private val repository: PlaylistScreenRepository): PlaylistScreenInteractor {

    override fun getPlaylistTracks(trackIds: List<Int>): Flow<List<Track>> {
        return repository.getPlaylistTracks(trackIds)
    }
}