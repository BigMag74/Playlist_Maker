package com.practicum.playlist_maker.mediaLibrary.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlist_maker.mediaLibrary.ui.FavoriteScreenState
import com.practicum.playlist_maker.mediaLibrary.ui.view_model.FavoriteTracksFragmentViewModel
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.player.ui.activity.AudioPlayerFragment
import com.practicum.playlist_maker.search.ui.TrackAdapter
import com.practicum.playlist_maker.search.ui.TracksClickListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {

    private var placeholderText: TextView? = null
    private var placeholderImage: ImageView? = null
    private var recyclerView: RecyclerView? = null

    private var trackAdapter: TrackAdapter? = null

    private val favoriteTracksFragmentViewModel: FavoriteTracksFragmentViewModel by viewModel()

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        placeholderText = binding.placeholderText
        placeholderImage = binding.placeholderImage
        recyclerView = binding.favoriteTracksRecyclerView

        favoriteTracksFragmentViewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        val onClickListener = object : TracksClickListener {
            override fun onTrackClick(track: Track) {
                val bundle = Bundle()
                bundle.putString(TRACK, Gson().toJson(track))
                findNavController().navigate(
                    R.id.action_mediaLibraryFragment_to_audioPlayerActivity,
                    bundle
                )
            }
        }

        trackAdapter = TrackAdapter(onClickListener)
        recyclerView?.adapter = trackAdapter

    }

    override fun onResume() {
        super.onResume()
        favoriteTracksFragmentViewModel.loadTracks()
    }


    private fun render(state: FavoriteScreenState) {
        when (state) {
            is FavoriteScreenState.Empty -> {
                placeholderText?.visibility = View.VISIBLE
                placeholderImage?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
            }
            is FavoriteScreenState.Content -> {
                placeholderText?.visibility = View.GONE
                placeholderImage?.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE

                trackAdapter?.tracks?.clear()
                trackAdapter?.tracks?.addAll(state.tracks)
                trackAdapter?.notifyDataSetChanged()
            }
        }
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
        const val TRACK = "TRACK"
    }

}

