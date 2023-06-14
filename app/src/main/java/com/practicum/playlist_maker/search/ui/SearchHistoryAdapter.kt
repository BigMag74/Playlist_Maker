package com.practicum.playlist_maker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.player.domain.model.Track

class SearchHistoryAdapter(private val clickListener: TracksClickListener) :
    Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

    }

    override fun getItemCount() = tracks.size


}