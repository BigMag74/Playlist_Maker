package com.practicum.playlist_maker.search.di

import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {
    single<TracksInteractor> { TracksInteractorImpl(get()) }
}