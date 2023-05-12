package com.practicum.playlist_maker

import android.media.MediaPlayer
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
import com.practicum.playlist_maker.SearchActivity.Companion.TRACK
import java.text.SimpleDateFormat
import java.util.*


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

    private var mediaPlayer = MediaPlayer()
    private var url: String = ""
    private var playerState = STATE_DEFAULT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track: Track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)
        url = track.previewUrl

        handler = Handler(Looper.getMainLooper())

        initialize(track)

        preparePlayer()

        findViewById<ImageView>(R.id.backButton).setOnClickListener { onBackPressed() }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateTime())
        mediaPlayer.release()
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

        Glide.with(trackIcon)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.album)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(trackIcon)

        trackName.text = track.trackName
        artist.text = track.artistName
        playTime.text = getString(R.string.time00_00)
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)


        if (track.collectionName == "${track.trackName} - $SINGLE") {
            albumRight.visibility = View.GONE
            albumLeft.visibility = View.GONE
        } else {
            albumRight.visibility = View.VISIBLE
            albumLeft.visibility = View.VISIBLE
            albumRight.text = track.collectionName
        }

        val calendar: Calendar = GregorianCalendar()
        calendar.time =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate) as Date
        year.text = calendar.get(Calendar.YEAR).toString()

        genre.text = track.primaryGenreName
        country.text = track.country

        playButton.setOnClickListener { playbackControl() }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                handler.post(updateTime())
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play_button)
            playerState = STATE_PREPARED
            handler.removeCallbacks(updateTime())
            playTime.text = getString(R.string.time00_00)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
        handler.removeCallbacks(updateTime())
    }

    private fun updateTime(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == STATE_PLAYING) {
                    playTime.text = SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, 400L)
                }
            }

        }
    }

    companion object {
        private const val SINGLE = "Single"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}