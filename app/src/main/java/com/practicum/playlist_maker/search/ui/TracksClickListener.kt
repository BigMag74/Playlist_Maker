package com.practicum.playlist_maker.search.ui

import com.practicum.playlist_maker.player.domain.model.Track

interface TracksClickListener {
    fun onTrackClick(track: Track)
}