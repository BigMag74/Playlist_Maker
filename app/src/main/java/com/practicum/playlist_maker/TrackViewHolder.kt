package com.practicum.playlist_maker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : ViewHolder(itemView) {
    private val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
    private val trackHint: TextView = itemView.findViewById(R.id.trackHint)
    private val trackIcon: ImageView = itemView.findViewById(R.id.trackIcon)


    fun bind(model: Track) {
        trackTitle.text = model.trackName
        trackHint.text = model.artistName + "  ●  " + model.trackTime

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.playlists)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(trackIcon)

    }
}