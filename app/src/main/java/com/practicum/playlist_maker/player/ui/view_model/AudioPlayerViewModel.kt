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

    private val stateLiveData = MutableLiveData<AudioPlayerState>()
    fun observeState(): LiveData<AudioPlayerState> = stateLiveData

    private var timerJob: Job? = null


    init {
        renderState(AudioPlayerState.Default())
        prepareAudioPlayer()
        setOnCompleteListener()
    }

    private fun prepareAudioPlayer() {
        audioPlayer.preparePlayer(url = trackUrl) {
            renderState(AudioPlayerState.Prepared())
        }
    }


    private fun startAudioPlayer() {
        audioPlayer.startPlayer()
        renderState(AudioPlayerState.Playing(getCurrentPosition()))
        startTimer()
    }

    private fun pauseAudioPlayer() {
        audioPlayer.pausePlayer()
        timerJob?.cancel()
        renderState(AudioPlayerState.Paused(getCurrentPosition()))
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayer.isPlaying()) {
                delay(300L)
                renderState(AudioPlayerState.Playing(getCurrentPosition()))
            }
        }
    }

    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(audioPlayer.getCurrentPosition()) ?: "00:00"
    }

    private fun setOnCompleteListener() {
        audioPlayer.setOnCompletionListener {
            renderState(AudioPlayerState.Prepared())
            timerJob?.cancel()
        }
    }

    fun playbackControl() {
        when (stateLiveData.value) {
            is AudioPlayerState.Playing -> {
                pauseAudioPlayer()
            }
            is AudioPlayerState.Prepared, is AudioPlayerState.Paused -> {
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