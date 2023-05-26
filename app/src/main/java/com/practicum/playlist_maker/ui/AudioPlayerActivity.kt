package com.practicum.playlist_maker.ui

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
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.Track
import com.practicum.playlist_maker.domain.usecase.GetTrackUseCase
import com.practicum.playlist_maker.presentation.AudioPlayerPresenter
import com.practicum.playlist_maker.presentation.AudioPlayerView
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerActivity : AppCompatActivity(), AudioPlayerView {

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

    private lateinit var audioPlayerPresenter: AudioPlayerPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        track = GetTrackUseCase(intent).execute()
        url = track.previewUrl

        handler = Handler(Looper.getMainLooper())

        initialize(track)

        audioPlayerPresenter = AudioPlayerPresenter(url, this as AudioPlayerView)

        findViewById<ImageView>(R.id.backButton).setOnClickListener { onBackPressed() }
    }

    override fun onPause() {
        super.onPause()
        audioPlayerPresenter.pauseAudioPlayer()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerPresenter.destroyAudioPlayer()
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
            audioPlayerPresenter.playbackControl()
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
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
    }

    private fun initializeYear() {
        val calendar: Calendar = GregorianCalendar()
        calendar.time =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate) as Date
        year.text = calendar.get(Calendar.YEAR).toString()
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
                playTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(audioPlayerPresenter.getCurrentPosition())
                handler.postDelayed(this, 400L)

            }

        }
    }

    companion object {
        private const val SINGLE = "Single"
    }

    override fun changePlayButtonImageToPlay() {
        playButton.setImageResource(R.drawable.play_button)
    }

    override fun changePlayButtonImageToPause() {
        playButton.setImageResource(R.drawable.pause_button)
    }

    override fun activatePlayButton() {
        playButton.isEnabled = true
    }

    override fun onCompletePlaying() {
        handler.removeCallbacksAndMessages(null)
        playTime.text = getString(R.string.time00_00)
    }

    override fun updateHandler() {
        handler.post(updateTime())
    }

}