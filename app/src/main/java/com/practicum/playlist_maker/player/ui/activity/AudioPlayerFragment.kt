package com.practicum.playlist_maker.player.ui.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentAudioPlayerBinding
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.player.ui.AddTrackToPlaylistState
import com.practicum.playlist_maker.player.ui.AudioPlayerAdapter
import com.practicum.playlist_maker.player.ui.AudioPlayerPlaylistsState
import com.practicum.playlist_maker.player.ui.AudioPlayerState
import com.practicum.playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.practicum.playlist_maker.search.ui.activity.SearchFragment
import com.practicum.playlist_maker.utils.DateTimeUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerFragment : Fragment() {

    private var trackIcon: ImageView? = null
    private var trackName: TextView? = null
    private var artist: TextView? = null
    private var playTime: TextView? = null
    private var duration: TextView? = null
    private var albumRight: TextView? = null
    private var albumLeft: TextView? = null
    private var year: TextView? = null
    private var genre: TextView? = null
    private var country: TextView? = null
    private var playButton: ImageView? = null
    private var addToPlaylistButton: FloatingActionButton? = null
    private var likeButton: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
    private var bottomSheet: LinearLayout? = null
    private var overlay: View? = null
    private var createNewPlaylistButton: Button? = null

    private var playlistAdapter: AudioPlayerAdapter? = null

    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    private var url: String? = null
    private lateinit var track: Track
    private val audioPlayerViewModel: AudioPlayerViewModel by viewModel { parametersOf(track) }

    private var _binding: FragmentAudioPlayerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = Gson().fromJson(arguments?.getString(SearchFragment.TRACK), Track::class.java)
        url = track.previewUrl

        initialize(track)
        initializeBottomSheet()
        setOnClickListeners()


        audioPlayerViewModel.state.observe(viewLifecycleOwner) {
            render(it)
        }

        audioPlayerViewModel.playlistsState.observe(viewLifecycleOwner) {
            renderPlaylist(it)
        }

        audioPlayerViewModel.addTrackToPlaylistState.observe(viewLifecycleOwner) {
            renderAddTrackToPlaylist(it)
        }

        playlistAdapter = AudioPlayerAdapter()
        playlistAdapter?.onPlayListClicked = {
            audioPlayerViewModel.addTrackToPlaylist(track, it)
        }
        recyclerView?.adapter = playlistAdapter
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

    private fun setOnClickListeners() {
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        addToPlaylistButton?.setOnClickListener {
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        createNewPlaylistButton?.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerActivity_to_creationPlaylistFragment)
        }
    }

    private fun initializeBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet!!).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior?.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay?.visibility = View.GONE
                    }
                    else -> {
                        audioPlayerViewModel.loadPlaylists()
                        overlay?.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun initialize(track: Track) {
        trackIcon = binding.trackIcon
        trackName = binding.trackName
        artist = binding.artistName
        playTime = binding.playTime
        duration = binding.durationRight
        albumRight = binding.albumRight
        albumLeft = binding.albumLeft
        year = binding.yearRight
        genre = binding.genreRight
        country = binding.countryRight
        playButton = binding.playButton
        addToPlaylistButton = binding.addToPlaylistButton
        likeButton = binding.likeButton
        recyclerView = binding.bottomSheetRecyclerView
        bottomSheet = binding.bottomSheet
        overlay = binding.overlay
        createNewPlaylistButton = binding.createNewPlaylistButton

        initializeIcon()
        trackName?.text = track.trackName
        artist?.text = track.artistName
        playTime?.text = getString(R.string.time00_00)
        initializeDuration()
        initializeAlbum()
        initializeYear()
        genre?.text = track.primaryGenreName
        country?.text = track.country

        playButton?.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }

        likeButton?.setOnClickListener {
            audioPlayerViewModel.onFavoriteClicked()
        }
    }

    private fun initializeIcon() {
        trackIcon?.let {
            Glide.with(it)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.album)
                .centerCrop()
                .transform(RoundedCorners(8))
                .into(trackIcon!!)
        }
    }

    private fun initializeDuration() {
        duration?.text = DateTimeUtil.msecToMMSS(track.trackTimeMillis)

    }

    private fun initializeYear() {
        year?.text = DateTimeUtil.stringToYear(track.releaseDate)
    }

    private fun initializeAlbum() {
        if (track.collectionName == "${track.trackName} - $SINGLE") {
            albumRight?.visibility = View.GONE
            albumLeft?.visibility = View.GONE
        } else {
            albumRight?.visibility = View.VISIBLE
            albumLeft?.visibility = View.VISIBLE
            albumRight?.text = track.collectionName
        }
    }

    private fun render(state: AudioPlayerState) {
        playButton?.isEnabled = state.isPlayButtonEnabled
        playTime?.text = state.progress
        setLikeImage()
        when (state) {
            is AudioPlayerState.Default, is AudioPlayerState.Prepared, is AudioPlayerState.Paused -> {
                changePlayButtonImageToPlay()
            }
            is AudioPlayerState.Playing -> {
                changePlayButtonImageToPause()
            }
        }
    }

    private fun renderPlaylist(state: AudioPlayerPlaylistsState) {
        when (state) {
            is AudioPlayerPlaylistsState.EmptyPlaylists -> {
                playlistAdapter?.playlists?.clear()
                playlistAdapter?.notifyDataSetChanged()
            }
            is AudioPlayerPlaylistsState.ContentPlaylists -> {
                playlistAdapter?.playlists?.clear()
                playlistAdapter?.playlists?.addAll(state.playlists)
                playlistAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun renderAddTrackToPlaylist(state: AddTrackToPlaylistState) {
        when (state) {
            is AddTrackToPlaylistState.AlreadyAdded -> {
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.track_has_already_been_added_to_the_playlist)} ${state.playlist.name}",
                    Toast.LENGTH_LONG
                ).show()
            }
            is AddTrackToPlaylistState.AddedNow -> {
                Toast.makeText(
                    requireContext(),
                    "${getString(R.string.added_to_playlist)} ${state.playlist.name}",
                    Toast.LENGTH_LONG
                ).show()
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun changePlayButtonImageToPlay() {
        playButton?.setImageResource(R.drawable.play_button)
    }

    private fun changePlayButtonImageToPause() {
        playButton?.setImageResource(R.drawable.pause_button)
    }

    private fun setLikeImage() {
        if (track.isFavorite) {
            changeLikeButtonImageToRed()
        } else {
            changeLikeButtonImageToWhite()
        }
    }

    private fun changeLikeButtonImageToWhite() {
        likeButton?.setImageResource(R.drawable.like_button_not_pressed)
        likeButton?.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
    }

    private fun changeLikeButtonImageToRed() {
        likeButton?.setImageResource(R.drawable.like_button_pressed)
        likeButton?.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.red_light))
    }

    companion object {
        private const val SINGLE = "Single"
    }
}