package com.practicum.playlist_maker.mediaLibrary.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlist_maker.mediaLibrary.ui.view_model.PlaylistFragmentViewModel
import com.practicum.playlist_maker.databinding.FragmnetPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private val playlistFragmentViewModel: PlaylistFragmentViewModel by viewModel()

    private lateinit var binding: FragmnetPlaylistsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmnetPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistFragmentViewModel.observe()
    }

}