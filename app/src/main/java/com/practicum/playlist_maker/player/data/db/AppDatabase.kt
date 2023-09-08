package com.practicum.playlist_maker.player.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlist_maker.creationPlaylist.data.db.dao.PlaylistDao
import com.practicum.playlist_maker.creationPlaylist.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.player.data.db.dao.FavoriteTrackDao
import com.practicum.playlist_maker.player.data.db.dao.TrackInPlaylistsDao
import com.practicum.playlist_maker.player.data.db.entity.FavoriteTrackEntity
import com.practicum.playlist_maker.player.data.db.entity.TrackInPlaylistsEntity

@Database(
    version = 8,
    entities = [FavoriteTrackEntity::class, PlaylistEntity::class, TrackInPlaylistsEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteTrackDao(): FavoriteTrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun trackInPlaylistDao(): TrackInPlaylistsDao
}