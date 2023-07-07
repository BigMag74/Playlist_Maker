package com.practicum.playlist_maker.mediaLibrary.di

import com.practicum.playlist_maker.mediaLibrary.ui.view_model.FavoriteTracksFragmentViewModel
import com.practicum.playlist_maker.mediaLibrary.ui.view_model.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryModule = module {

    viewModel { FavoriteTracksFragmentViewModel() }
    viewModel { PlaylistFragmentViewModel() }

}