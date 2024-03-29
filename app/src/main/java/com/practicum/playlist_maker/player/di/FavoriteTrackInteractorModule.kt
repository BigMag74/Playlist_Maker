package com.practicum.playlist_maker.player.di

import com.practicum.playlist_maker.player.data.impl.FavoriteTracksRepositoryImpl
import com.practicum.playlist_maker.player.domain.api.FavoriteTrackInteractor
import com.practicum.playlist_maker.player.domain.api.FavoriteTracksRepository
import com.practicum.playlist_maker.player.domain.impl.FavoriteTrackInteractorImpl
import org.koin.dsl.module

val favoriteTrackInteractorModule = module {
    single<FavoriteTracksRepository> { FavoriteTracksRepositoryImpl(get()) }
    single<FavoriteTrackInteractor> { FavoriteTrackInteractorImpl(get()) }
}