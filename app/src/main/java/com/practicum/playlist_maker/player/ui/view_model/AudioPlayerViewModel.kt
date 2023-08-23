package com.practicum.playlist_maker.player.ui.view_model

import androidx.lifecycle.*
import com.practicum.playlist_maker.player.domain.api.AudioPlayer
import com.practicum.playlist_maker.player.ui.AudioPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(private val audioPlayer: AudioPlayer, private val trackUrl: String) :
    ViewModel() {

    private val _state = MutableLiveData<AudioPlayerState>()
    val state: LiveData<AudioPlayerState> = _state

    private var timerJob: Job? = null


    init {
        setState(AudioPlayerState.Default())
        prepareAudioPlayer()
        setOnCompleteListener()
    }

    private fun prepareAudioPlayer() {
        audioPlayer.preparePlayer(url = trackUrl) {
            setState(AudioPlayerState.Prepared())
        }
    }


    private fun startAudioPlayer() {
        audioPlayer.startPlayer()
        setState(AudioPlayerState.Playing(getCurrentTrackTimePosition()))
        startTimer()
    }

    private fun pauseAudioPlayer() {
        audioPlayer.pausePlayer()
        timerJob?.cancel()
        setState(AudioPlayerState.Paused(getCurrentTrackTimePosition()))
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayer.isPlaying()) {
                delay(UPDATE_DELAY_MILLIS)
                setState(AudioPlayerState.Playing(getCurrentTrackTimePosition()))
            }
        }
    }

    private fun getCurrentTrackTimePosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(audioPlayer.getCurrentPosition()) ?: "00:00"
    }

    private fun setOnCompleteListener() {
        audioPlayer.setOnCompletionListener {
            setState(AudioPlayerState.Prepared())
            timerJob?.cancel()
        }
    }

    fun playbackControl() {
        when (_state.value) {
            is AudioPlayerState.Playing -> {
                pauseAudioPlayer()
            }
            is AudioPlayerState.Prepared, is AudioPlayerState.Paused -> {
                startAudioPlayer()
            }
            else -> {}
        }
    }

    private fun setState(state: AudioPlayerState) {
        _state.postValue(state)
    }


    override fun onCleared() {
        audioPlayer.destroyPlayer()
    }

    companion object {
        const val UPDATE_DELAY_MILLIS = 300L
    }


}