package com.practicum.playlist_maker.playlist.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.creationPlaylist.domain.model.Playlist
import com.practicum.playlist_maker.databinding.FragmentPlaylistScreenBinding
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.playlist.ui.PlaylistScreenAdapter
import com.practicum.playlist_maker.playlist.ui.PlaylistScreenState
import com.practicum.playlist_maker.playlist.ui.PlaylistScreenTracksState
import com.practicum.playlist_maker.playlist.ui.view_model.PlaylistScreenViewModel
import com.practicum.playlist_maker.search.ui.TracksClickListener
import com.practicum.playlist_maker.search.ui.activity.SearchFragment
import com.practicum.playlist_maker.utils.DateTimeUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class PlaylistScreenFragment : Fragment() {

    private var backButton: ImageView? = null
    private var playlistImage: ImageView? = null
    private var playlistName: TextView? = null
    private var playlistDescription: TextView? = null
    private var playlistTime: TextView? = null
    private var playlistCountOfTracks: TextView? = null
    private var shareButton: ImageView? = null
    private var optionsButton: ImageView? = null
    private var bottomSheetContainer: LinearLayout? = null
    private var bottomSheetEditContainer: LinearLayout? = null
    private var recyclerView: RecyclerView? = null
    private var bottomSheetEditPlaylistImage: ImageView? = null
    private var bottomSheetEditPlaylistName: TextView? = null
    private var bottomSheetEditPlaylistCountOfTracks: TextView? = null
    private var bottomSheetEditShare: TextView? = null
    private var bottomSheetEditModify: TextView? = null
    private var bottomSheetEditDelete: TextView? = null
    private var overlay: View? = null
    private var tracksPlaceholder: TextView? = null

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int? = null
    private var playlist: Playlist? = null
    private var trackIds: MutableList<Int>? = mutableListOf()

    private var trackAdapter: PlaylistScreenAdapter? = null

    private var dialog: MaterialAlertDialogBuilder? = null
    private var dialogDeletePlaylist: MaterialAlertDialogBuilder? = null

    private val viewModel: PlaylistScreenViewModel by viewModel { parametersOf(playlistId) }

    private lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private lateinit var behaviorEdit: BottomSheetBehavior<LinearLayout>

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
        viewModel.tracksState.observe(viewLifecycleOwner) {
            renderTracks(it)
        }

        initialiseViews()
        setOnClickListeners()

        trackAdapter = PlaylistScreenAdapter(onClickListener)
        trackAdapter?.onLongClickListener = {
            dialog = MaterialAlertDialogBuilder(requireContext(), R.style.dialog_style)
                .setTitle(getString(R.string.do_you_want_to_delete_track))
                .setNegativeButton(getString(R.string.no_caps)) { _, _ -> }
                .setPositiveButton(getString(R.string.yes_caps)) { _, _ ->
                    playlist?.let { it1 -> viewModel.deleteTrackFromPlaylist(it.trackId, it1) }
                }
            dialog?.show()
        }
        recyclerView?.adapter = trackAdapter



        behaviorEdit.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay?.visibility = View.GONE
                    }
                    else -> {
                        overlay?.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

    }

    private fun initialiseViews() {
        backButton = binding.backButton
        playlistImage = binding.playlistImage
        playlistName = binding.playlistName
        playlistDescription = binding.playlistDescription
        playlistTime = binding.playlistTime
        playlistCountOfTracks = binding.playlistCountOfTracks
        shareButton = binding.shareButton
        optionsButton = binding.optionsButton
        bottomSheetContainer = binding.bottomSheet
        bottomSheetEditContainer = binding.bottomSheetEditContainer
        recyclerView = binding.bottomSheetRecyclerView
        bottomSheetEditPlaylistImage = binding.bottomSheetEditPlaylistImage
        bottomSheetEditPlaylistName = binding.bottomSheetEditPlaylistName
        bottomSheetEditPlaylistCountOfTracks = binding.bottomSheetEditPlaylistCountOfTracks
        bottomSheetEditShare = binding.bottomSheetEditShare
        bottomSheetEditModify = binding.bottomSheetEditModify
        bottomSheetEditDelete = binding.bottomSheetEditDelete
        overlay = binding.overlay
        tracksPlaceholder = binding.tracksPlaceholder

        behavior = BottomSheetBehavior.from(bottomSheetContainer!!)
        behaviorEdit = BottomSheetBehavior.from(bottomSheetEditContainer!!)
        behaviorEdit.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private val onShareClickListener = {
        if (playlist?.countOfTracks == null || playlist?.countOfTracks!! == 0) {
            Toast.makeText(
                requireContext(),
                getString(R.string.there_is_no_track_list_to_share_in_this_playlist),
                Toast.LENGTH_LONG
            ).show()
        } else {
            var trackList = ""
            trackAdapter?.tracks!!.forEachIndexed { index, track ->
                trackList += "${index + 1}. ${track.artistName} - ${track.trackName} (${
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(track.trackTimeMillis)
                })\n"
            }
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val text: String = "${getString(R.string.playlist)}: ${playlist?.name}\n" +
                    (if (!playlist?.description.isNullOrEmpty()) {
                        playlist?.description + "\n"
                    } else
                        "") +
                    "${playlist?.countOfTracks} ${getString(DateTimeUtil.trackWordEndingId(playlist?.countOfTracks!!))}\n" +
                    trackList

            intent.putExtra(Intent.EXTRA_TEXT, text)

            startActivityOrShowErrorMessage(
                intent,
                getString(R.string.there_is_no_app_on_the_device_to_make_this_request)
            )
        }
    }

    private val onClickListener = object : TracksClickListener {
        override fun onTrackClick(track: Track) {
            val bundle = Bundle()
            bundle.putString(SearchFragment.TRACK, Gson().toJson(track))
            findNavController().navigate(
                R.id.action_playlistFragment_to_audioPlayerActivity,
                bundle
            )
        }
    }

    private fun setOnClickListeners() {

        backButton?.setOnClickListener { findNavController().navigateUp() }
        shareButton?.setOnClickListener { onShareClickListener.invoke() }

        optionsButton?.setOnClickListener {
            bottomSheetEditPlaylistImage?.let {
                Glide.with(it)
                    .load(playlist?.pathUri)
                    .placeholder(R.drawable.album)
                    .centerCrop()
                    .into(bottomSheetEditPlaylistImage!!)
            }
            bottomSheetEditPlaylistName?.text = playlist?.name
            bottomSheetEditPlaylistCountOfTracks?.text =
                "${playlist?.countOfTracks.toString()} ${
                    playlist?.countOfTracks?.let { it1 ->
                        getString(
                            DateTimeUtil.trackWordEndingId(
                                it1
                            )
                        )
                    }
                }"
            behaviorEdit.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetEditShare?.setOnClickListener { onShareClickListener.invoke() }

        bottomSheetEditDelete?.setOnClickListener {
            dialogDeletePlaylist =
                MaterialAlertDialogBuilder(requireContext(), R.style.dialog_style)
                    .setTitle("${getString(R.string.do_you_want_to_delete_playlist)} «${playlist?.name}»?")
                    .setNegativeButton(getString(R.string.no_caps)) { _, _ -> }
                    .setPositiveButton(getString(R.string.yes_caps)) { _, _ ->
                        playlist?.let { it1 -> viewModel.deletePlaylist(it1) }
                        findNavController().navigateUp()
                    }
            dialogDeletePlaylist?.show()
        }

        bottomSheetEditModify?.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(PLAYLIST, Gson().toJson(playlist))
            findNavController().navigate(
                R.id.action_playlistFragment_to_creationPlaylistFragment,
                bundle
            )
        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility =
            View.GONE
        viewModel.loadPlaylist()
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

    private fun startActivityOrShowErrorMessage(intent: Intent, message: String) {
        try {
            startActivity(intent)
            behaviorEdit.state = BottomSheetBehavior.STATE_HIDDEN
        } catch (e: Exception) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

    }

    private fun render(state: PlaylistScreenState) {
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
                    "${playlist?.countOfTracks.toString()} ${
                        playlist?.countOfTracks?.let { it1 ->
                            getString(
                                DateTimeUtil.trackWordEndingId(
                                    it1
                                )
                            )
                        }
                    }"

                viewModel.loadTracks(playlist?.trackIds as List<Int>)
                trackIds = playlist?.trackIds
            }
        }
    }

    private fun renderTracks(state: PlaylistScreenTracksState) {
        when (state) {
            is PlaylistScreenTracksState.ContentState -> {
                trackAdapter?.tracks?.clear()
                trackAdapter?.tracks?.addAll(state.tracks)
                trackAdapter?.notifyDataSetChanged()
                val minutes = SimpleDateFormat(
                    "mm",
                    Locale.getDefault()
                ).format(viewModel.calculatePlaylistTime(state.tracks))
                playlistTime?.text =
                    "$minutes ${getString(DateTimeUtil.minuteWordEndingId(minutes.toInt()))}"
                tracksPlaceholder?.visibility = View.GONE
                recyclerView?.visibility = View.VISIBLE
            }
            is PlaylistScreenTracksState.EmptyState -> {
                recyclerView?.visibility = View.GONE
                tracksPlaceholder?.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val PLAYLIST = "playlist"
    }
}