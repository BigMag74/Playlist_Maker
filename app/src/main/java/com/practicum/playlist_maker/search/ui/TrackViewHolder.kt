package com.practicum.playlist_maker.search.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.utils.DateTimeUtil

class TrackViewHolder(itemView: View, private val clickListener: TracksClickListener) :
    ViewHolder(itemView) {
    private val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
    private val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val trackIcon: ImageView = itemView.findViewById(R.id.trackIcon)

    var onLongClickListener: ((track: Track) -> Unit)? = null


    fun bind(model: Track) {
        trackTitle.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = DateTimeUtil.msecToMMSS(model.trackTimeMillis)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.album)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(trackIcon)

        itemView.setOnClickListener { clickListener.onTrackClick(model) }
        itemView.setOnLongClickListener {
            onLongClickListener?.invoke(model)
            true
        }

    }
}