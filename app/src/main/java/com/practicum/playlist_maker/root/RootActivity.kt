package com.practicum.playlist_maker.root

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.ActivityRootBinding
import com.practicum.playlist_maker.main.ui.MainFragment

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                this.add(R.id.rootFragmentContainerView, MainFragment.newInstance())
            }
        }
    }
}