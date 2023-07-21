package com.practicum.playlist_maker.mediaLibrary.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlist_maker.mediaLibrary.ui.MediaLibraryViewPagerAdapter
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentMediaLibraryBinding
import com.practicum.playlist_maker.main.ui.MainFragment

class MediaLibraryFragment : Fragment() {

    private lateinit var binding: FragmentMediaLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator

    companion object {
        fun newInstance() = MediaLibraryFragment()
        const val TAG = "MediaLibraryFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMediaLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.rootFragmentContainerView, MainFragment.newInstance())
            }
        }

        binding.viewPager.adapter = MediaLibraryViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }

        tabMediator.attach()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }
}