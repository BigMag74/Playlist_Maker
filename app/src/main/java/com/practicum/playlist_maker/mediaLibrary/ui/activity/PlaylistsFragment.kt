package com.practicum.playlist_maker.mediaLibrary.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentPlaylistsBinding
import com.practicum.playlist_maker.mediaLibrary.ui.PlaylistFragmentAdapter
import com.practicum.playlist_maker.mediaLibrary.ui.PlaylistFragmentState
import com.practicum.playlist_maker.mediaLibrary.ui.view_model.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var createNewPlaylistButton: Button? = null
    private var placeholderImage: ImageView? = null
    private var placeholderText: TextView? = null
    private var recyclerView: RecyclerView? = null

    private val playlistFragmentViewModel: PlaylistFragmentViewModel by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private var playlistAdapter: PlaylistFragmentAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistFragmentViewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        createNewPlaylistButton = binding.createNewPlaylistButton
        placeholderImage = binding.placeholderImage
        placeholderText = binding.placeholderText
        recyclerView = binding.playlistsRecyclerView

        playlistAdapter = PlaylistFragmentAdapter()
        recyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView?.adapter = playlistAdapter

        createNewPlaylistButton?.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_creationPlaylistFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        playlistFragmentViewModel.loadPlaylists()
    }

    private fun render(state: PlaylistFragmentState) {
        when (state) {
            is PlaylistFragmentState.EmptyPlaylists -> {
                placeholderImage?.visibility = View.VISIBLE
                placeholderText?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE

                playlistAdapter?.playlists?.clear()
                playlistAdapter?.notifyDataSetChanged()
            }
            is PlaylistFragmentState.ContentPlaylists -> {
                placeholderImage?.visibility = View.GONE
                placeholderText?.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE

                playlistAdapter?.playlists?.clear()
                playlistAdapter?.playlists?.addAll(state.playlists)
                playlistAdapter?.notifyDataSetChanged()
            }
        }
    }


    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}