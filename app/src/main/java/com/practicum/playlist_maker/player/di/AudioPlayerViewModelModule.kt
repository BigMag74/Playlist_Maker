package com.practicum.playlist_maker.player.di

import com.practicum.playlist_maker.player.ui.view_model.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val audioPlayerViewModelModule = module {
    viewModel { (trackUrl: String) -> AudioPlayerViewModel(get(), trackUrl) }
}