package com.practicum.playlist_maker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter

class TrackAdapter : Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()
    var onItemClick: ((Track) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tracks[position])
        }
    }

    override fun getItemCount() = tracks.size

}