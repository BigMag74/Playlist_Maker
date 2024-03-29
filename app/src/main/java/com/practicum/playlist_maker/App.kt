package com.practicum.playlist_maker

import android.app.Application
import com.practicum.playlist_maker.creationPlaylist.di.creationPlaylistModule
import com.practicum.playlist_maker.mediaLibrary.di.mediaLibraryModule
import com.practicum.playlist_maker.mediaLibrary.di.playlistFragmentModule
import com.practicum.playlist_maker.player.di.audioPlayerDataModule
import com.practicum.playlist_maker.player.di.audioPlayerModule
import com.practicum.playlist_maker.player.di.audioPlayerViewModelModule
import com.practicum.playlist_maker.player.di.favoriteTrackInteractorModule
import com.practicum.playlist_maker.search.di.searchDataModule
import com.practicum.playlist_maker.search.di.searchInteractorModule
import com.practicum.playlist_maker.search.di.searchRepositoryModule
import com.practicum.playlist_maker.search.di.searchViewModelModule
import com.practicum.playlist_maker.settings.di.settingsInteractorModule
import com.practicum.playlist_maker.settings.di.settingsRepositoryModule
import com.practicum.playlist_maker.settings.di.settingsViewModelModule
import com.practicum.playlist_maker.settings.domain.api.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val KEY_FOR_SWITCH = "key_for_switch"
const val SETTINGS_PREFERENCES = "settings_preferences"

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                searchDataModule,
                searchInteractorModule,
                searchRepositoryModule,
                searchViewModelModule,
                audioPlayerDataModule,
                audioPlayerModule,
                audioPlayerViewModelModule,
                favoriteTrackInteractorModule,
                settingsInteractorModule,
                settingsRepositoryModule,
                settingsViewModelModule,
                mediaLibraryModule,
                creationPlaylistModule,
                playlistFragmentModule,
            )

        }

        val settingsInteractor: SettingsInteractor by inject()
        settingsInteractor.switchTheme(settingsInteractor.isDarkTheme())

    }



}