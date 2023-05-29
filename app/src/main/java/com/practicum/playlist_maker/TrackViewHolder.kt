package com.practicum.playlist_maker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlist_maker.domain.model.Track
import com.practicum.playlist_maker.utils.DateTimeUtil

class TrackViewHolder(itemView: View) : ViewHolder(itemView) {
    private val trackTitle: TextView = itemView.findViewById(R.id.trackTitle)
    private val trackArtist: TextView = itemView.findViewById(R.id.trackArtist)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)
    private val trackIcon: ImageView = itemView.findViewById(R.id.trackIcon)


    fun bind(model: Track) {
        trackTitle.text = model.trackName
        trackArtist.text = model.artistName
        trackTime.text = DateTimeUtil().msecToMMSS(model.trackTimeMillis)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.album)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(trackIcon)

    }
}