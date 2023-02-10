package com.practicum.playlist_maker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchButton = findViewById<Button>(R.id.search_btn)
        val mediaLibraryButton = findViewById<Button>(R.id.media_library_btn)
        val settingsButton = findViewById<Button>(R.id.settings_btn)

        val intentForSearchButton = Intent(this, SearchActivity::class.java)
        val searchClickListener: OnClickListener = object : OnClickListener {
            override fun onClick(v: View?) {
                startActivity(intentForSearchButton)
            }
        }
        searchButton.setOnClickListener(searchClickListener)
        mediaLibraryButton.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}