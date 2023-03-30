package com.practicum.playlist_maker

import android.content.SharedPreferences
import com.google.gson.Gson


class SearchHistory(private val sharedPreferences: SharedPreferences) {
    val TRACK_LIST_KEY = "track_list_key"
    fun getTracks(): Array<Track> {
        val json = sharedPreferences.getString(TRACK_LIST_KEY, null) ?: return emptyArray()
        val tracks = Gson().fromJson(json, Array<Track>::class.java)
        return tracks.reversedArray()
    }

    fun addTrack(track: Track) {
        val tracks = read().toMutableList()
        var theSame = false

        for (element in tracks) {
            if (element.trackId == track.trackId) {
                tracks.remove(element)
                theSame = true
                break
            }
        }
        if (!theSame && tracks.size >= 10) {
            tracks.removeAt(9)
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