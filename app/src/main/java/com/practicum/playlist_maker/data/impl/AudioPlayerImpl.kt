package com.practicum.playlist_maker.data.impl

import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlist_maker.domain.api.AudioPlayer


class AudioPlayerImpl : AudioPlayer {
    private val audioPlayer = MediaPlayer()

    override fun preparePlayer(url: String, onPreparedListener: () -> Unit) {
        audioPlayer.setDataSource(url)
        audioPlayer.prepareAsync()
        audioPlayer.setOnPreparedListener {
            onPreparedListener()
        }
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        audioPlayer.setOnCompletionListener { onCompletionListener() }
    }

    override fun getCurrentPosition(): Int {
        return audioPlayer.currentPosition
    }

    override fun startPlayer() {
        audioPlayer.start()
    }

    override fun pausePlayer() {
        audioPlayer.pause()
    }

    override fun destroyPlayer() {
        audioPlayer.release()
    }

}