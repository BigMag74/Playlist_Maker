package com.practicum.playlist_maker.mediaLibrary.ui

import com.practicum.playlist_maker.player.domain.model.Track


sealed class FavoriteScreenState() {

    class Empty : FavoriteScreenState()

    data class Content(val tracks: List<Track>) : FavoriteScreenState()

}