package com.practicum.playlist_maker.mediaLibrary.di

import com.practicum.playlist_maker.mediaLibrary.data.impl.PlaylistFragmentRepositoryImpl
import com.practicum.playlist_maker.mediaLibrary.domain.db.PlaylistFragmentInteractor
import com.practicum.playlist_maker.mediaLibrary.domain.db.PlaylistFragmentRepository
import com.practicum.playlist_maker.mediaLibrary.domain.impl.PlaylistFragmentInteractorImpl
import com.practicum.playlist_maker.mediaLibrary.ui.view_model.PlaylistFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistFragmentModule = module {

    single<PlaylistFragmentInteractor> { PlaylistFragmentInteractorImpl(get()) }
    single<PlaylistFragmentRepository> { PlaylistFragmentRepositoryImpl(get()) }
    viewModel { PlaylistFragmentViewModel(get()) }
}