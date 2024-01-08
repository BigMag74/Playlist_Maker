package com.practicum.playlist_maker.playlist.ui

import android.view.LayoutInflater
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.search.ui.TrackViewHolder
import com.practicum.playlist_maker.search.ui.TracksClickListener

class PlaylistScreenAdapter(private val clickListener: TracksClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var tracks = ArrayList<Track>()

    var onLongClickListener: ((track: Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_layout, parent, false)
        return TrackViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.onLongClickListener = onLongClickListener
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
}