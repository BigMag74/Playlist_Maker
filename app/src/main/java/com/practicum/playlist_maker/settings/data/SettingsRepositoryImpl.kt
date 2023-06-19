package com.practicum.playlist_maker.settings.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlist_maker.KEY_FOR_SWITCH
import com.practicum.playlist_maker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {
    private var darkTheme = sharedPreferences.getBoolean(KEY_FOR_SWITCH, false)
    override fun isDarkTheme(): Boolean {
        return darkTheme
    }

    override fun switchTheme(isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            darkTheme = true
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            darkTheme = false
        }
        sharedPreferences.edit().putBoolean(KEY_FOR_SWITCH, darkTheme).apply()
    }
}