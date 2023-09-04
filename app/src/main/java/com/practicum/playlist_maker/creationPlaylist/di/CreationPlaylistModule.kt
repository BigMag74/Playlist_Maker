package com.practicum.playlist_maker.creationPlaylist.di

import com.practicum.playlist_maker.creationPlaylist.data.impl.CreationPlaylistRepositoryImpl
import com.practicum.playlist_maker.creationPlaylist.domain.db.CreationPlaylistInteractor
import com.practicum.playlist_maker.creationPlaylist.domain.db.CreationPlaylistRepository
import com.practicum.playlist_maker.creationPlaylist.domain.impl.CreationPlaylistInteractorImpl
import com.practicum.playlist_maker.creationPlaylist.ui.view_model.CreationPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val creationPlaylistModule = module {

    single<CreationPlaylistRepository> { CreationPlaylistRepositoryImpl(get()) }
    single<CreationPlaylistInteractor> { CreationPlaylistInteractorImpl(get()) }
    viewModel { CreationPlaylistViewModel(get()) }
}