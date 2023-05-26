package com.practicum.playlist_maker.presentation

interface AudioPlayerView {

    fun changePlayButtonImageToPlay()
    fun changePlayButtonImageToPause()

    fun activatePlayButton()

    fun onCompletePlaying()

    fun updateHandler()
}