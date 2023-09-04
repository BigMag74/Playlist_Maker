package com.practicum.playlist_maker.creationPlaylist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creationPlaylist.domain.db.CreationPlaylistInteractor
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.creationPlaylist.ui.CreationPlaylistState
import com.practicum.playlist_maker.mediaLibrary.ui.FavoriteScreenState
import kotlinx.coroutines.launch

class CreationPlaylistViewModel(private val interactor: CreationPlaylistInteractor) :
    ViewModel() {
    private val _state = MutableLiveData<CreationPlaylistState>()
    val state: LiveData<CreationPlaylistState> = _state

    init {
        setState(CreationPlaylistState.EmptyName())
    }

    fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactor.addPlaylist(playlist)
        }
    }


    private fun setState(state: CreationPlaylistState) {
        _state.postValue(state)
    }
}