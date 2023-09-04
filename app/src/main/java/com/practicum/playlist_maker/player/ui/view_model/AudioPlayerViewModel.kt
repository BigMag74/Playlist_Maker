package com.practicum.playlist_maker.player.ui.view_model

import androidx.lifecycle.*
import com.practicum.playlist_maker.mediaLibrary.domain.db.PlaylistFragmentInteractor
import com.practicum.playlist_maker.player.domain.api.AudioPlayer
import com.practicum.playlist_maker.player.domain.db.FavoriteTrackInteractor
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.player.ui.AudioPlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(
    private val audioPlayer: AudioPlayer,
    private val track: Track,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val playlistFragmentInteractor: PlaylistFragmentInteractor
) :
    ViewModel() {

    private val _state = MutableLiveData<AudioPlayerState>()
    val state: LiveData<AudioPlayerState> = _state

    private var timerJob: Job? = null


    init {
        setState(AudioPlayerState.Default(track.isFavorite))
        setFavoriteTrack()
        prepareAudioPlayer()
        setOnCompleteListener()
    }

    private fun prepareAudioPlayer() {
        audioPlayer.preparePlayer(url = track.previewUrl) {
            setState(AudioPlayerState.Prepared(track.isFavorite))
        }
    }


    private fun startAudioPlayer() {
        audioPlayer.startPlayer()
        setState(AudioPlayerState.Playing(getCurrentTrackTimePosition(),track.isFavorite))
        startTimer()
    }

    private fun pauseAudioPlayer() {
        audioPlayer.pausePlayer()
        timerJob?.cancel()
        setState(AudioPlayerState.Paused(getCurrentTrackTimePosition(),track.isFavorite))
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayer.isPlaying()) {
                delay(UPDATE_DELAY_MILLIS)
                setState(AudioPlayerState.Playing(getCurrentTrackTimePosition(),track.isFavorite))
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
            setState(AudioPlayerState.Prepared(track.isFavorite))
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

    private fun setFavoriteTrack() {
        viewModelScope.launch {
            favoriteTrackInteractor.getFavoriteTrackIds().collect() {
                state.value?.isFavorite = it.contains(track.trackId)
                track.isFavorite = it.contains(track.trackId)
            }
        }
    }

    fun onFavoriteClicked() {
        if (track.isFavorite) {
            viewModelScope.launch { favoriteTrackInteractor.deleteTrackFromFavorite(track) }
        } else {
            viewModelScope.launch { favoriteTrackInteractor.addTrackToFavorite(track) }
        }

        track.isFavorite = !track.isFavorite
        setState(state.value!!)
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