package com.practicum.playlist_maker.domain.api


interface AudioPlayer {
    fun preparePlayer(url: String, onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun getCurrentPosition(): Int

    fun startPlayer()

    fun pausePlayer()

    fun destroyPlayer()
}