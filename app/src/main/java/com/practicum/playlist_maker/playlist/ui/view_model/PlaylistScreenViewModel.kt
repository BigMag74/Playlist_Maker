package com.practicum.playlist_maker.playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creationPlaylist.domain.api.CreationPlaylistInteractor
import com.practicum.playlist_maker.playlist.domain.api.PlaylistScreenInteractor
import com.practicum.playlist_maker.playlist.ui.PlaylistScreenState
import kotlinx.coroutines.launch

class PlaylistScreenViewModel(
    private val playlistId: Int,
    private val playlistInteractor: CreationPlaylistInteractor,
    private val tracksInteractor: PlaylistScreenInteractor,
) :
    ViewModel() {

    private var _state = MutableLiveData<PlaylistScreenState>()
    val state: LiveData<PlaylistScreenState> = _state

    init {
        loadPlaylist()
    }

    fun loadPlaylist() {
        viewModelScope.launch {
            setState(PlaylistScreenState.BasedState(playlistInteractor.getPlaylistById(playlistId)))
        }
    }


    private fun setState(state: PlaylistScreenState) {
        _state.postValue(state)
    }

}