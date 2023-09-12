package com.practicum.playlist_maker.player.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlist_maker.player.data.db.entity.TrackInPlaylistsEntity

@Dao
interface TrackInPlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistsEntity)

    @Query("SELECT * FROM track_in_playlists_table ORDER BY created_at DESC")
    suspend fun getAllTracks(): List<TrackInPlaylistsEntity>
}