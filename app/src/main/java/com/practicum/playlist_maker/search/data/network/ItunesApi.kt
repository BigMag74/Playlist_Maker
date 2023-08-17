package com.practicum.playlist_maker.search.data.network

import com.practicum.playlist_maker.search.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity=song")
    suspend fun getTracks(@Query("term") text: String): TracksSearchResponse
}