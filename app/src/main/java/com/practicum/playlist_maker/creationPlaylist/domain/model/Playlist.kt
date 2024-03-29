package com.practicum.playlist_maker.creationPlaylist.domain.model

import android.net.Uri

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String = "",
    val pathUri: Uri? = null,
    val trackIds: MutableList<Int> = mutableListOf(),
    val countOfTracks: Int = 0,
)