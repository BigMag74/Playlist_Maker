package com.practicum.playlist_maker.presentation

import com.practicum.playlist_maker.data.impl.AudioPlayerImpl
import com.practicum.playlist_maker.domain.api.AudioPlayer

class AudioPlayerPresenter(private val url: String, private val audioPlayerView: AudioPlayerView) {

    private val audioPlayer: AudioPlayer = AudioPlayerImpl()

    private var playerState = STATE_DEFAULT

    init {
        prepareAudioPlayer()
        setOnCompleteListener()
    }

    private fun prepareAudioPlayer() {
        audioPlayer.preparePlayer(url = url) {
            audioPlayerView.activatePlayButton()
            playerState = STATE_PREPARED
        }
    }

    private fun startAudioPlayer() {
        audioPlayer.startPlayer()
        playerState = STATE_PLAYING
        audioPlayerView.changePlayButtonImageToPause()
    }

    fun pauseAudioPlayer() {
        audioPlayer.pausePlayer()
        playerState = STATE_PAUSED
        audioPlayerView.changePlayButtonImageToPlay()
    }

    fun destroyAudioPlayer() {
        audioPlayer.destroyPlayer()
    }

    fun getCurrentPosition(): Int {
        return audioPlayer.getCurrentPosition()
    }

    private fun setOnCompleteListener() {
        audioPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            audioPlayerView.changePlayButtonImageToPlay()
            audioPlayerView.onCompletePlaying()
        }
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pauseAudioPlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startAudioPlayer()
                audioPlayerView.updateHandler()
            }
        }
    }


    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}