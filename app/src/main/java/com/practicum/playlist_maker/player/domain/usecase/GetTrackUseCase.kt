package com.practicum.playlist_maker.player.domain.usecase

import android.content.Intent
import com.google.gson.Gson
import com.practicum.playlist_maker.search.ui.activity.SearchActivity
import com.practicum.playlist_maker.player.domain.model.Track

class GetTrackUseCase(private val intent: Intent) {

    fun execute(): Track {
        return Gson().fromJson(intent.getStringExtra(SearchActivity.TRACK), Track::class.java)
    }
}