package com.practicum.playlist_maker.playlist.di

import com.practicum.playlist_maker.playlist.data.impl.PlaylistScreenRepositoryImpl
import com.practicum.playlist_maker.playlist.domain.api.PlaylistScreenInteractor
import com.practicum.playlist_maker.playlist.domain.api.PlaylistScreenRepository
import com.practicum.playlist_maker.playlist.domain.impl.PlaylistScreenInteractorImpl
import com.practicum.playlist_maker.playlist.ui.view_model.PlaylistScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistScreenModule = module {

    single<PlaylistScreenRepository> { PlaylistScreenRepositoryImpl(get()) }
    single<PlaylistScreenInteractor> { PlaylistScreenInteractorImpl(get()) }
    viewModel { (playlistId: Int) -> PlaylistScreenViewModel(playlistId, get(), get()) }
}