package com.practicum.playlist_maker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlist_maker.SearchActivity.Companion.TRACK
import java.text.SimpleDateFormat
import java.time.LocalDate
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track: Track = Gson().fromJson(intent.getStringExtra(TRACK), Track::class.java)

        initialize(track)

        findViewById<ImageView>(R.id.backButton).setOnClickListener { onBackPressed() }
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

        Glide.with(trackIcon)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.playlists)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(trackIcon)

        trackName.text = track.trackName
        artist.text = track.artistName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

        if (track.collectionName.substring(track.collectionName.length - 6 until track.collectionName.length) == "Single") {
            albumRight.visibility = View.GONE
            albumLeft.visibility = View.GONE
        } else {
            albumRight.visibility = View.VISIBLE
            albumLeft.visibility = View.VISIBLE
            albumRight.text = track.collectionName
        }

        year.text = track.releaseDate.substring(0..3)
        genre.text = track.primaryGenreName
        country.text = track.country
    }
}