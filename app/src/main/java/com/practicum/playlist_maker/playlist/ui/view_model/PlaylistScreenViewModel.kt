package com.practicum.playlist_maker.playlist.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creationPlaylist.domain.api.CreationPlaylistInteractor
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.playlist.domain.api.PlaylistScreenInteractor
import com.practicum.playlist_maker.playlist.ui.PlaylistScreenState
import com.practicum.playlist_maker.playlist.ui.PlaylistScreenTracksState
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val playlistId: Int,
    private val playlistInteractor: CreationPlaylistInteractor,
    private val tracksInteractor: PlaylistScreenInteractor,
) :
    ViewModel() {

    private var _state = MutableLiveData<PlaylistScreenState>()
    val state: LiveData<PlaylistScreenState> = _state

    private var _tracksState = MutableLiveData<PlaylistScreenTracksState>()
    val tracksState: LiveData<PlaylistScreenTracksState> = _tracksState

    init {
        loadPlaylist()
    }

    fun loadPlaylist() {
        viewModelScope.launch {
            setState(PlaylistScreenState.BasedState(playlistInteractor.getPlaylistById(playlistId)))
        }
    }

    fun loadTracks(trackIds: List<Int>) {
        viewModelScope.launch {
            tracksInteractor.getPlaylistTracks(trackIds).collect { tracks ->
                setTracksState(PlaylistScreenTracksState.BasedState(tracks))
            }
        }
    }

    fun calculatePlaylistTime(tracks: List<Track>): Long {
        var res: Long = 0
        for (track in tracks) {
            res += track.trackTimeMillis
        }
        return res
    }

    fun deleteTrackFromPlaylist(trackId: Int, playlist: Playlist) {
        viewModelScope.launch {
            tracksInteractor.deleteTrackFromPlaylist(trackId, playlist)
            loadPlaylist()
        }
    }

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            tracksInteractor.deletePlaylist(playlist)
        }
    }


    private fun setState(state: PlaylistScreenState) {
        _state.postValue(state)
    }

    private fun setTracksState(state: PlaylistScreenTracksState) {
        _tracksState.postValue(state)
    }

}