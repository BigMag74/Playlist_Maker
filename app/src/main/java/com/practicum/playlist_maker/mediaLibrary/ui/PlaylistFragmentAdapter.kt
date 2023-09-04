package com.practicum.playlist_maker.mediaLibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

class PlaylistFragmentAdapter() :
    RecyclerView.Adapter<PlaylistFragmentViewHolder>() {
    var playlists: ArrayList<Playlist> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistFragmentViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_layout_for_media_library, parent, false)
        return PlaylistFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistFragmentViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}