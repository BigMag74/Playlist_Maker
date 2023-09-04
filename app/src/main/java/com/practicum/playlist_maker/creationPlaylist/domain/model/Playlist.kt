package com.practicum.playlist_maker.creationPlaylist.domain.model

import android.net.Uri

data class Playlist(
    val name: String,
    val description: String = "",
    val pathUri: Uri? = null,
    val trackIds: List<Int>? = null,
    val countOfTracks: Int = 0,
)