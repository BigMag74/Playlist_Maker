package com.practicum.playlist_maker.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.practicum.playlist_maker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}