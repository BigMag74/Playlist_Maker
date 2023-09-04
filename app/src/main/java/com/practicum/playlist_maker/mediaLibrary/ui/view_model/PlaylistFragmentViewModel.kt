package com.practicum.playlist_maker.mediaLibrary.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.creationPlaylist.ui.CreationPlaylistState
import com.practicum.playlist_maker.mediaLibrary.domain.db.PlaylistFragmentInteractor
import com.practicum.playlist_maker.mediaLibrary.ui.PlaylistFragmentState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(private val interactor: PlaylistFragmentInteractor) : ViewModel() {

    private var _state = MutableLiveData<PlaylistFragmentState>()
    val state: LiveData<PlaylistFragmentState> = _state

    init {
        loadPlaylists()
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            interactor.getPlaylists().collect() { playlists ->
                if(playlists.isEmpty()){
                    setState(PlaylistFragmentState.EmptyPlaylists())
                }
                else{
                    setState(PlaylistFragmentState.ContentPlaylists(playlists))
                }
            }
        }
    }


    private fun setState(state: PlaylistFragmentState) {
        _state.postValue(state)
    }

}