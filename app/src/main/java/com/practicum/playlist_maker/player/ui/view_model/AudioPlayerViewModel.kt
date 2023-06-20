package com.practicum.playlist_maker.player.ui.view_model

import androidx.lifecycle.*
import com.practicum.playlist_maker.player.domain.api.AudioPlayer
import com.practicum.playlist_maker.player.ui.AudioPlayerState

class AudioPlayerViewModel(private val audioPlayer: AudioPlayer, private val trackUrl: String) :
    ViewModel() {

    private val stateLiveData = MutableLiveData<AudioPlayerState>()
    fun observeState(): LiveData<AudioPlayerState> = stateLiveData


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


}