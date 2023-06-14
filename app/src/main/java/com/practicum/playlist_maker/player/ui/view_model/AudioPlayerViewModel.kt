package com.practicum.playlist_maker.player.ui.view_model

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.player.data.impl.AudioPlayerImpl
import com.practicum.playlist_maker.player.domain.api.AudioPlayer
import com.practicum.playlist_maker.player.domain.usecase.GetTrackUseCase
import com.practicum.playlist_maker.player.ui.AudioPlayerState

class AudioPlayerViewModel(application: Application) :
    AndroidViewModel(application) {

    private val stateLiveData = MutableLiveData<AudioPlayerState>()
    fun observeState(): LiveData<AudioPlayerState> = stateLiveData

    private val audioPlayer: AudioPlayer = Creator.provideAudioPlayer()
    private var trackUrl = ""

    init {
        renderState(AudioPlayerState.STATE_DEFAULT)
        getTrackUrl()
        prepareAudioPlayer()
        setOnCompleteListener()
    }

    private fun prepareAudioPlayer() {
        audioPlayer.preparePlayer(url = trackUrl) {
            renderState(AudioPlayerState.STATE_PREPARED)
        }
    }

    private fun getTrackUrl() {
        trackUrl = GetTrackUseCase(myIntent!!).execute().previewUrl
    }

    private fun startAudioPlayer() {
        audioPlayer.startPlayer()
        renderState(AudioPlayerState.STATE_PLAYING(audioPlayer.getCurrentPosition()))
    }

    private fun pauseAudioPlayer() {
        audioPlayer.pausePlayer()
        renderState(AudioPlayerState.STATE_PAUSED)
    }


    fun getCurrentPosition(): Int {
        return audioPlayer.getCurrentPosition()
    }

    private fun setOnCompleteListener() {
        audioPlayer.setOnCompletionListener {
            renderState(AudioPlayerState.STATE_PREPARED)
        }
    }

    fun playbackControl() {
        when (stateLiveData.value) {
            is AudioPlayerState.STATE_PLAYING -> {
                pauseAudioPlayer()
            }
            is AudioPlayerState.STATE_PREPARED, AudioPlayerState.STATE_PAUSED -> {
                startAudioPlayer()
            }
            else -> {}
        }
    }

    private fun renderState(state: AudioPlayerState) {
        stateLiveData.postValue(state)
    }

    fun onPause() {
        pauseAudioPlayer()
        renderState(AudioPlayerState.STATE_PAUSED)
    }

    override fun onCleared() {
        audioPlayer.destroyPlayer()
    }


    companion object {
        private var myIntent: Intent? = null
        fun getViewModelFactory(intent: Intent): ViewModelProvider.Factory = viewModelFactory() {
            myIntent = intent
            initializer {
                AudioPlayerViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

}