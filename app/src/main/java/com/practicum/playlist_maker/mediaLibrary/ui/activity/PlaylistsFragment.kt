package com.practicum.playlist_maker.mediaLibrary.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentPlaylistsBinding
import com.practicum.playlist_maker.mediaLibrary.ui.view_model.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private var createNewPlaylistButton: Button? = null

    private val playlistFragmentViewModel: PlaylistFragmentViewModel by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistFragmentViewModel.observe()
        createNewPlaylistButton = binding.createNewPlaylistButton

        createNewPlaylistButton?.setOnClickListener {
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_creationPlaylistFragment)
        }
    }


    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}