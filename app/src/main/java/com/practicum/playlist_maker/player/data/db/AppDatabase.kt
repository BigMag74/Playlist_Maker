package com.practicum.playlist_maker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlist_maker.player.data.db.dao.TrackDao
import com.practicum.playlist_maker.player.data.db.entity.TrackEntity

@Database(version = 3, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}