package com.practicum.playlist_maker.settings.domain

interface SettingsInteractor {
    fun isDarkTheme(): Boolean
    fun switchTheme(isChecked: Boolean)
}