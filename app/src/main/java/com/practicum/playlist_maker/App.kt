package com.practicum.playlist_maker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val KEY_FOR_SWITCH = "key_for_switch"
const val SETTINGS_PREFERENCES = "settings_preferences"

class App : Application() {
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(KEY_FOR_SWITCH, false)
        switchTheme(darkTheme)
    }


    fun switchTheme(darkThemeEnabled: Boolean) {
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