package com.practicum.playlist_maker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

class TrackAdapter(private val searchHistory: SearchHistory) : Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()
    var onItemClick = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            searchHistory.addTrack(tracks[position])
            onItemClick.invoke()
        }
    }

    override fun getItemCount() = tracks.size

}