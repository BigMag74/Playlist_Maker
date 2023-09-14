package com.practicum.playlist_maker.creationPlaylist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creationPlaylist.domain.api.CreationPlaylistInteractor
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.creationPlaylist.ui.CreationPlaylistState
import com.practicum.playlist_maker.creationPlaylist.ui.EditPlaylistState
import kotlinx.coroutines.launch

class CreationPlaylistViewModel(
    private val interactor: CreationPlaylistInteractor,
    private val playlist: Playlist?
) :
    ViewModel() {
    private val _creationState = MutableLiveData<CreationPlaylistState>()
    val creationState: LiveData<CreationPlaylistState> = _creationState

    private val _editState = MutableLiveData<EditPlaylistState>()
    val editState: LiveData<EditPlaylistState> = _editState

    init {
        if (playlist != null) {
            setEditState(EditPlaylistState.JustOpened(playlist))
        } else {
            setCreationState(CreationPlaylistState.EmptyName())
        }
    }

    fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactor.addPlaylist(playlist)
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            interactor.updatePlaylist(playlist)
        }
    }

    private fun setCreationState(state: CreationPlaylistState) {
        _creationState.postValue(state)
    }

    private fun setEditState(state: EditPlaylistState) {
        _editState.postValue(state)
    }
}