package com.practicum.playlist_maker.player.ui.view_model

import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.player.domain.api.AudioPlayer
import com.practicum.playlist_maker.player.ui.AudioPlayerState

class AudioPlayerViewModel() :
    ViewModel() {

    private val stateLiveData = MutableLiveData<AudioPlayerState>()
    fun observeState(): LiveData<AudioPlayerState> = stateLiveData

    private val audioPlayer: AudioPlayer = Creator.provideAudioPlayer()

    init {
        renderState(AudioPlayerState.STATE_DEFAULT)
        prepareAudioPlayer()
        setOnCompleteListener()
    }

    private fun prepareAudioPlayer() {
        audioPlayer.preparePlayer(url = trackUrl) {
            renderState(AudioPlayerState.STATE_PREPARED)
        }
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


    override fun onCleared() {
        audioPlayer.destroyPlayer()
    }


    companion object {
        private var trackUrl: String = ""

        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory() {
            trackUrl = url
            initializer {
                AudioPlayerViewModel()
            }
        }
    }

}