package com.practicum.playlist_maker.player.ui


sealed class AudioPlayerState(
    val isPlayButtonEnabled: Boolean,
    val progress: String
) {

    class Default : AudioPlayerState(false, "00:00")

    class Prepared : AudioPlayerState(true, "00:00")

    class Playing(progress: String) : AudioPlayerState(true, progress)

    class Paused(progress: String) : AudioPlayerState(true, progress)
}
