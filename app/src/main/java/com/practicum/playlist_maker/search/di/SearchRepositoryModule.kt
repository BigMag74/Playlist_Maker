package com.practicum.playlist_maker.search.di

import com.practicum.playlist_maker.search.data.TracksRepositoryImpl
import com.practicum.playlist_maker.search.domain.api.TracksRepository
import org.koin.dsl.module

val searchRepositoryModule = module {
    single<TracksRepository> { TracksRepositoryImpl(get(), get()) }
}