package com.practicum.playlist_maker.player.ui

sealed class FavoriteButtonState {
    class Liked() : FavoriteButtonState()
    class UnLiked() : FavoriteButtonState()
}