package com.practicum.playlist_maker.settings.di

import com.practicum.playlist_maker.settings.domain.api.SettingsInteractor
import com.practicum.playlist_maker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val settingsInteractorModule = module {
    single<SettingsInteractor> { SettingsInteractorImpl(get()) }
}