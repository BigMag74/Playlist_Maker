package com.practicum.playlist_maker.player.data.db.dao

import androidx.room.*
import com.practicum.playlist_maker.player.data.db.entity.TrackInPlaylistsEntity

@Dao
interface TrackInPlaylistsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistsEntity)

    @Query("SELECT * FROM track_in_playlists_table ORDER BY created_at DESC")
    suspend fun getAllTracks(): List<TrackInPlaylistsEntity>

    @Query("DELETE FROM track_in_playlists_table WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Int)
}