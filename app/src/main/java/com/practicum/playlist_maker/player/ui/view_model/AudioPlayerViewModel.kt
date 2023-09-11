package com.practicum.playlist_maker.player.ui.view_model

import androidx.lifecycle.*
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.mediaLibrary.domain.api.PlaylistFragmentInteractor
import com.practicum.playlist_maker.player.domain.api.AudioPlayer
import com.practicum.playlist_maker.player.domain.api.FavoriteTrackInteractor
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.player.ui.AddTrackToPlaylistState
import com.practicum.playlist_maker.player.ui.AudioPlayerPlaylistsState
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

    private var _state = MutableLiveData<AudioPlayerState>()
    val state: LiveData<AudioPlayerState> = _state

    private var _playlistsState = MutableLiveData<AudioPlayerPlaylistsState>()
    val playlistsState: LiveData<AudioPlayerPlaylistsState> = _playlistsState

    private val _addTrackToPlaylistState = MutableLiveData<AddTrackToPlaylistState>()
    val addTrackToPlaylistState: LiveData<AddTrackToPlaylistState> = _addTrackToPlaylistState

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
        setState(AudioPlayerState.Playing(getCurrentTrackTimePosition(), track.isFavorite))
        startTimer()
    }

    private fun pauseAudioPlayer() {
        audioPlayer.pausePlayer()
        timerJob?.cancel()
        setState(AudioPlayerState.Paused(getCurrentTrackTimePosition(), track.isFavorite))
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayer.isPlaying()) {
                delay(UPDATE_DELAY_MILLIS)
                setState(AudioPlayerState.Playing(getCurrentTrackTimePosition(), track.isFavorite))
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

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistFragmentInteractor.getPlaylists().collect() { playlists ->
                if (playlists.isEmpty()) {
                    setPlaylistState(AudioPlayerPlaylistsState.EmptyPlaylists())
                } else {
                    setPlaylistState(AudioPlayerPlaylistsState.ContentPlaylists(playlists))
                }
            }
        }
    }

    fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        if (playlist.trackIds.contains(track.trackId)) {
            setAddTrackToPlaylistState(AddTrackToPlaylistState.AlreadyAdded(playlist))
        } else {
            viewModelScope.launch {
                playlistFragmentInteractor.addTrackToPlaylist(track, playlist)
            }
            setAddTrackToPlaylistState(AddTrackToPlaylistState.AddedNow(playlist))
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

    private fun setPlaylistState(state: AudioPlayerPlaylistsState) {
        _playlistsState.postValue(state)
    }

    private fun setAddTrackToPlaylistState(state: AddTrackToPlaylistState) {
        _addTrackToPlaylistState.postValue(state)
    }

    override fun onCleared() {
        audioPlayer.destroyPlayer()
    }

    companion object {
        const val UPDATE_DELAY_MILLIS = 300L
    }


}