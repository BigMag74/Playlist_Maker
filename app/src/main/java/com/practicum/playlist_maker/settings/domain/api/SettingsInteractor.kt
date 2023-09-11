package com.practicum.playlist_maker.settings.domain.api

interface SettingsInteractor {
    fun isDarkTheme(): Boolean
    fun switchTheme(isChecked: Boolean)
}