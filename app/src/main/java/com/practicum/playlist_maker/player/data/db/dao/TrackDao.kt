package com.practicum.playlist_maker.player.data.db.dao

import androidx.room.*
import com.practicum.playlist_maker.player.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY created_at DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM track_table")
    suspend fun getTracksId(): List<Int>
}