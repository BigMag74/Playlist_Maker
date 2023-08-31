package com.practicum.playlist_maker.mediaLibrary.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlist_maker.mediaLibrary.ui.FavoriteScreenState
import com.practicum.playlist_maker.player.domain.db.FavoriteTrackInteractor
import com.practicum.playlist_maker.player.domain.model.Track
import kotlinx.coroutines.launch

class FavoriteTracksFragmentViewModel(private val favoriteTrackInteractor: FavoriteTrackInteractor) :
    ViewModel() {

    private var tracks: List<Track> = emptyList()
    private val _state = MutableLiveData<FavoriteScreenState>()
    val state: LiveData<FavoriteScreenState> = _state

    init {
        loadTracks()
    }

    fun loadTracks() {
        viewModelScope.launch {
            favoriteTrackInteractor.getFavoriteTracks().collect { tracks ->
                if (tracks.isEmpty()) {
                    setState(FavoriteScreenState.Empty())
                } else {
                    setState(FavoriteScreenState.Content(tracks))
                }
            }
        }
    }


    private fun setState(state: FavoriteScreenState) {
        _state.postValue(state)
    }

}