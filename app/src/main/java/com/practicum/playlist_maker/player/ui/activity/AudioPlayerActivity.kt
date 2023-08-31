package com.practicum.playlist_maker.player.ui.activity

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlist_maker.databinding.ActivityRootBinding
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.player.ui.AudioPlayerState
import com.practicum.playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.practicum.playlist_maker.search.ui.activity.SearchFragment
import com.practicum.playlist_maker.utils.DateTimeUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerActivity : AppCompatActivity() {

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
    private var likeButton: FloatingActionButton? = null


    private var url: String? = null
    private lateinit var track: Track
    private val audioPlayerViewModel: AudioPlayerViewModel by viewModel { parametersOf(track) }

    private lateinit var binding: ActivityAudioPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = Gson().fromJson(intent.getStringExtra(SearchFragment.TRACK), Track::class.java)
        url = track.previewUrl


        initialize(track)

        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        audioPlayerViewModel.state.observe(this) {
            render(it)
        }
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
        likeButton = binding.likeButton

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