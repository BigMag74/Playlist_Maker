package com.practicum.playlist_maker.main.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.practicum.playlist_maker.R
import com.practicum.playlist_maker.databinding.FragmentMainBinding
import com.practicum.playlist_maker.mediaLibrary.ui.activity.MediaLibraryFragment
import com.practicum.playlist_maker.search.ui.activity.SearchFragment
import com.practicum.playlist_maker.settings.ui.activity.SettingsFragment

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()

    }

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.rootFragmentContainerView,
                    SearchFragment.newInstance(),
                    SearchFragment.TAG
                )
                addToBackStack(SearchFragment.TAG)
            }
        }

        binding.mediaLibraryButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.rootFragmentContainerView,
                    MediaLibraryFragment.newInstance(),
                    MediaLibraryFragment.TAG
                )
                addToBackStack(MediaLibraryFragment.TAG)
            }
        }

        binding.settingsButton.setOnClickListener {
            parentFragmentManager.commit {
                replace(
                    R.id.rootFragmentContainerView,
                    SettingsFragment.newInstance(),
                    SettingsFragment.TAG
                )
                addToBackStack(SettingsFragment.TAG)
            }
        }

    }

}