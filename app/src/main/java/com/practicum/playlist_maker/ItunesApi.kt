package com.practicum.playlist_maker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search?entity=song")
    fun getTrack(@Query("term") text: String): Call<TrackResponse>
}