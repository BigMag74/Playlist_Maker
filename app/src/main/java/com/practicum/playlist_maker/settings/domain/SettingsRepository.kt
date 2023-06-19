package com.practicum.playlist_maker.settings.domain

interface SettingsRepository {
    fun isDarkTheme(): Boolean
    fun switchTheme(isChecked: Boolean)
}