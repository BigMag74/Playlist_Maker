package com.practicum.playlist_maker.mediaLibrary.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlist_maker.mediaLibrary.ui.MediaLibraryViewPagerAdapter
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }

        }

        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}