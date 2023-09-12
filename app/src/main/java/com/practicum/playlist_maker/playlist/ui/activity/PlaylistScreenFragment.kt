package com.practicum.playlist_maker.playlist.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.databinding.FragmentPlaylistScreenBinding
import com.practicum.playlist_maker.mediaLibrary.ui.activity.PlaylistsFragment.Companion.PLAYLIST
import com.practicum.playlist_maker.playlist.ui.PlaylistScreenState
import com.practicum.playlist_maker.playlist.ui.view_model.PlaylistScreenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistScreenFragment : Fragment() {

    private var backButton: ImageView? = null
    private var playlistImage: ImageView? = null
    private var playlistName: TextView? = null
    private var playlistDescription: TextView? = null
    private var playlistTime: TextView? = null
    private var playlistCountOfTracks: TextView? = null
    private var shareButton: ImageView? = null
    private var optionsButton: ImageView? = null
    private var bottomSheet: LinearLayout? = null
    private var recyclerView: RecyclerView? = null

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int? = null
    private var playlist: Playlist? = null

    private val viewModel: PlaylistScreenViewModel by viewModel { parametersOf(playlistId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistId = Gson().fromJson(arguments?.getString(PLAYLIST), Int::class.java)

        viewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        backButton = binding.backButton
        playlistImage = binding.playlistImage
        playlistName = binding.playlistName
        playlistDescription = binding.playlistDescription
        playlistTime = binding.playlistTime
        playlistCountOfTracks = binding.playlistCountOfTracks
        shareButton = binding.shareButton
        optionsButton = binding.optionsButton
        bottomSheet = binding.bottomSheet
        recyclerView = binding.bottomSheetRecyclerView

        backButton?.setOnClickListener { findNavController().navigateUp() }


    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE
    }

    override fun onStop() {
        super.onStop()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val sourceTreeUri = data?.data
            context?.contentResolver?.takePersistableUriPermission(
                sourceTreeUri!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        }
    }

    private fun render(state: PlaylistScreenState?) {
        when (state) {
            is PlaylistScreenState.BasedState -> {
                playlist = state.playlist
                playlistImage?.let {
                    Glide.with(it)
                        .load(playlist?.pathUri)
                        .placeholder(R.drawable.album)
                        .centerCrop()
                        .into(playlistImage!!)
                }
                playlistName?.text = playlist?.name
                if (playlist?.description.isNullOrEmpty()) {
                    playlistDescription?.visibility = View.GONE
                } else {
                    playlistDescription?.visibility = View.VISIBLE
                    playlistDescription?.text = playlist?.description

                }
                playlistCountOfTracks?.text =
                    "${playlist?.countOfTracks.toString()} ${getString(R.string.tracks)}"
            }
            else -> {}
        }
    }
}