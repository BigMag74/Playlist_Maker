package com.practicum.playlist_maker.player.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.player.ui.AudioPlayerState
import com.practicum.playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.practicum.playlist_maker.search.ui.activity.SearchFragment
import com.practicum.playlist_maker.utils.DateTimeUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class AudioPlayerActivity : AppCompatActivity() {

    lateinit var trackIcon: ImageView
    lateinit var trackName: TextView
    lateinit var artist: TextView
    lateinit var playTime: TextView
    lateinit var duration: TextView
    lateinit var albumRight: TextView
    lateinit var albumLeft: TextView
    lateinit var year: TextView
    lateinit var genre: TextView
    lateinit var country: TextView
    lateinit var playButton: ImageView


    private lateinit var url: String
    private lateinit var track: Track
    private val audioPlayerViewModel: AudioPlayerViewModel by viewModel { parametersOf(url) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        track = Gson().fromJson(intent.getStringExtra(SearchFragment.TRACK), Track::class.java)
        url = track.previewUrl


        initialize(track)

        findViewById<ImageView>(R.id.backButton).setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        audioPlayerViewModel.state.observe(this) {
            render(it)
        }
    }


    private fun initialize(track: Track) {
        trackIcon = findViewById(R.id.trackIcon)
        trackName = findViewById(R.id.trackName)
        artist = findViewById(R.id.artistName)
        playTime = findViewById(R.id.playTime)
        duration = findViewById(R.id.durationRight)
        albumRight = findViewById(R.id.albumRight)
        albumLeft = findViewById(R.id.albumLeft)
        year = findViewById(R.id.yearRight)
        genre = findViewById(R.id.genreRight)
        country = findViewById(R.id.countryRight)
        playButton = findViewById(R.id.playButton)

        initializeIcon()
        trackName.text = track.trackName
        artist.text = track.artistName
        playTime.text = getString(R.string.time00_00)
        initializeDuration()
        initializeAlbum()
        initializeYear()
        genre.text = track.primaryGenreName
        country.text = track.country

        playButton.setOnClickListener {
            audioPlayerViewModel.playbackControl()
        }
    }

    private fun initializeIcon() {
        Glide.with(trackIcon)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(trackIcon)
    }

    private fun initializeDuration() {
        duration.text = DateTimeUtil.msecToMMSS(track.trackTimeMillis)

    }

    private fun initializeYear() {
        year.text = DateTimeUtil.stringToYear(track.releaseDate)
    }

    private fun initializeAlbum() {
        if (track.collectionName == "${track.trackName} - $SINGLE") {
            albumRight.visibility = View.GONE
            albumLeft.visibility = View.GONE
        } else {
            albumRight.visibility = View.VISIBLE
            albumLeft.visibility = View.VISIBLE
            albumRight.text = track.collectionName
        }
    }


    private fun render(state: AudioPlayerState) {
        playButton.isEnabled = state.isPlayButtonEnabled
        playTime.text = state.progress
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
        playButton.setImageResource(R.drawable.play_button)
    }

    private fun changePlayButtonImageToPause() {
        playButton.setImageResource(R.drawable.pause_button)
    }


    companion object {
        private const val SINGLE = "Single"
    }
}