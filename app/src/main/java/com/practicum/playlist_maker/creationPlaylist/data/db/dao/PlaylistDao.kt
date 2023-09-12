package com.practicum.playlist_maker.creationPlaylist.data.db.dao

import androidx.room.*
import com.practicum.playlist_maker.creationPlaylist.data.db.entity.PlaylistEntity
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId:Int): PlaylistEntity


}