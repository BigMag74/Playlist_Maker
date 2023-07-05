package com.practicum.playlist_maker.mediaLibrary.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteTracksFragmentViewModel : ViewModel() {

    private val liveData = MutableLiveData(true)
    fun observe(): LiveData<Boolean> = liveData

}