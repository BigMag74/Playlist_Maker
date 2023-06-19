package com.practicum.playlist_maker.player.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.player.domain.model.Track
import com.practicum.playlist_maker.player.ui.AudioPlayerState
import com.practicum.playlist_maker.player.ui.view_model.AudioPlayerViewModel
import com.practicum.playlist_maker.search.ui.activity.SearchActivity
import com.practicum.playlist_maker.utils.DateTimeUtil


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

    private lateinit var handler: Handler

    private var url: String = ""
    private lateinit var track: Track

    private lateinit var audioPlayerViewModel: AudioPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        track = Gson().fromJson(intent.getStringExtra(SearchActivity.TRACK), Track::class.java)
        url = track.previewUrl

        audioPlayerViewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory(url)
        )[AudioPlayerViewModel::class.java]


        handler = Handler(Looper.getMainLooper())

        initialize(track)

        findViewById<ImageView>(R.id.backButton).setOnClickListener { onBackPressed() }

        audioPlayerViewModel.observeState().observe(this) {
            render(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
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


    private fun updateTime(): Runnable {
        return object : Runnable {
            override fun run() {
                playTime.text = DateTimeUtil.msecToMMSS(audioPlayerViewModel.getCurrentPosition())
                handler.postDelayed(this, 400L)
            }

        }
    }

    private fun render(state: AudioPlayerState) {
        when (state) {
            is AudioPlayerState.STATE_DEFAULT -> {
                playButton.isEnabled = false
            }
            is AudioPlayerState.STATE_PREPARED -> {
                handler.removeCallbacksAndMessages(null)
                playTime.text = getString(R.string.time00_00)
                changePlayButtonImageToPlay()
                activatePlayButton()
            }
            is AudioPlayerState.STATE_PLAYING -> {
                changePlayButtonImageToPause()
                updateHandler()
            }
            is AudioPlayerState.STATE_PAUSED -> {
                changePlayButtonImageToPlay()
            }
        }
    }


    private fun changePlayButtonImageToPlay() {
        playButton.setImageResource(R.drawable.play_button)
    }

    private fun changePlayButtonImageToPause() {
        playButton.setImageResource(R.drawable.pause_button)
    }

    private fun activatePlayButton() {
        playButton.isEnabled = true
    }

    private fun updateHandler() {
        handler.post(updateTime())
    }

    companion object {
        private const val SINGLE = "Single"
    }
}