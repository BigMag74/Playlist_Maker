package com.practicum.playlist_maker.search.data.dto

import com.practicum.playlist_maker.player.domain.model.Track


class TracksSearchResponse(val resultCount: Int, val results: List<Track>) : Response()