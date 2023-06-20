package com.practicum.playlist_maker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlist_maker.player.di.audioPlayerModule
import com.practicum.playlist_maker.player.di.audioPlayerViewModelModule
import com.practicum.playlist_maker.search.di.searchDataModule
import com.practicum.playlist_maker.search.di.searchInteractorModule
import com.practicum.playlist_maker.search.di.searchRepositoryModule
import com.practicum.playlist_maker.search.di.searchViewModelModule
import com.practicum.playlist_maker.settings.di.settingsInteractorModule
import com.practicum.playlist_maker.settings.di.settingsRepositoryModule
import com.practicum.playlist_maker.settings.di.settingsViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val KEY_FOR_SWITCH = "key_for_switch"
const val SETTINGS_PREFERENCES = "settings_preferences"

class App : Application() {
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(KEY_FOR_SWITCH, false)
        switchTheme(darkTheme)


        startKoin {
            androidContext(this@App)
            modules(
                searchDataModule,
                searchInteractorModule,
                searchRepositoryModule,
                searchViewModelModule,
                audioPlayerModule,
                audioPlayerViewModelModule,
                settingsInteractorModule,
                settingsRepositoryModule,
                settingsViewModelModule,
            )

        }

    }


    private fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}