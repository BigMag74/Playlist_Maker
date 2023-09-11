package com.practicum.playlist_maker.settings.domain.api

interface SettingsRepository {
    fun isDarkTheme(): Boolean
    fun switchTheme(isChecked: Boolean)
}