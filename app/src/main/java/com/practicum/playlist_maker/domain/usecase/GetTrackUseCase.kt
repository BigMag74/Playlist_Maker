package com.practicum.playlist_maker.domain.usecase

import android.content.Intent
import com.google.gson.Gson
import com.practicum.playlist_maker.SearchActivity
import com.practicum.playlist_maker.domain.model.Track

class GetTrackUseCase(private val intent: Intent) {

    fun execute(): Track {
        return Gson().fromJson(intent.getStringExtra(SearchActivity.TRACK), Track::class.java)
    }
}