package com.practicum.playlist_maker.player.data.db.dao

import androidx.room.*
import com.practicum.playlist_maker.player.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteTrackEntity)

    @Delete(entity = FavoriteTrackEntity::class)
    suspend fun deleteTrack(track: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_track_table ORDER BY created_at DESC")
    suspend fun getTracks(): List<FavoriteTrackEntity>

    @Query("SELECT trackId FROM favorite_track_table")
    suspend fun getTracksId(): List<Int>
}