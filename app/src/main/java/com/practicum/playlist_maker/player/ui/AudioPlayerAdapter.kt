package com.practicum.playlist_maker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist

class AudioPlayerAdapter() :
    RecyclerView.Adapter<AudioPlayerViewHolder>() {

    var onPlayListClicked: ((playlist: Playlist) -> Unit)? = null

    var playlists: ArrayList<Playlist> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioPlayerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_layout_for_audio_player, parent, false)
        return AudioPlayerViewHolder(view, onPlayListClicked)
    }

    override fun onBindViewHolder(holder: AudioPlayerViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}