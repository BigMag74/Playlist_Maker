package com.practicum.playlist_maker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlist_maker.creationPlaylist.data.db.dao.PlaylistDao
import com.practicum.playlist_maker.creationPlaylist.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.player.data.db.dao.TrackDao
import com.practicum.playlist_maker.player.data.db.entity.TrackEntity

@Database(version = 4, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao
}