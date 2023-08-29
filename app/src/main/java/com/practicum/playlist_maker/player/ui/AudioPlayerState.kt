package com.practicum.playlist_maker.player.ui


sealed class AudioPlayerState(
    val isPlayButtonEnabled: Boolean,
    val progress: String,
    var isFavorite: Boolean,
) {

    class Default(isFavorite: Boolean) : AudioPlayerState(false, "00:00", isFavorite)

    class Prepared(isFavorite: Boolean) : AudioPlayerState(true, "00:00", isFavorite)

    class Playing(progress: String, isFavorite: Boolean) : AudioPlayerState(true, progress, isFavorite)

    class Paused(progress: String, isFavorite: Boolean) : AudioPlayerState(true, progress,isFavorite)
}
