package com.practicum.playlist_maker.player.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

class AudioPlayerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val playlistImage: ShapeableImageView = itemView.findViewById(R.id.playlistImage)
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val playlistCountOfTracks: TextView = itemView.findViewById(R.id.playlistCountOfTracks)


    fun bind(playlist: Playlist) {

        Glide.with(itemView)
            .load(playlist.pathUri) // <- Здесь
            .placeholder(R.drawable.album)
            .centerCrop()
            .into(playlistImage)

        playlistName.text = playlist.name
        playlistCountOfTracks.text =
            "${playlist.countOfTracks} ${itemView.context.getString(R.string.tracks)}"
    }
}