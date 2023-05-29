package com.practicum.playlist_maker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlist_maker.domain.model.Track


class SearchHistory(private val sharedPreferences: SharedPreferences) {
    val TRACK_LIST_KEY = "track_list_key"
    fun getTracks(): Array<Track> {
        val json = sharedPreferences.getString(TRACK_LIST_KEY, null) ?: return emptyArray()
        val tracks = Gson().fromJson(json, Array<Track>::class.java)
        return tracks.reversedArray()
    }

    fun addTrack(track: Track) {
        val tracks = read().toMutableList()

        if (tracks.contains(track)) {
            tracks.remove(track)
        }

        if (tracks.size >= 10) {
            tracks.removeAt(0)
        }
        tracks.add(track)

        sharedPreferences.edit()
            .putString(TRACK_LIST_KEY, Gson().toJson(tracks))
            .apply()
    }

    fun clearHistory() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    private fun read(): Array<Track> {
        val json = sharedPreferences.getString(TRACK_LIST_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }


}