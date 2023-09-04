package com.practicum.playlist_maker.creationPlaylist.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val path: String,
    val trackIds: String,
    val countOfTracks: Int,
)