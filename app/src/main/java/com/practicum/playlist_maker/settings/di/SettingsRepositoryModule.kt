package com.practicum.playlist_maker.settings.di

import android.content.Context
import com.practicum.playlist_maker.SETTINGS_PREFERENCES
import com.practicum.playlist_maker.settings.data.SettingsRepositoryImpl
import com.practicum.playlist_maker.settings.domain.api.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsRepositoryModule = module {

    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    single {
        androidContext().getSharedPreferences(
            SETTINGS_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
}