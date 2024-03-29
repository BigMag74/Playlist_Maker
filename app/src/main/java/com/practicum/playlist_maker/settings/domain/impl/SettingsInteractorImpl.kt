package com.practicum.playlist_maker.settings.domain.impl


import com.practicum.playlist_maker.settings.domain.api.SettingsInteractor
import com.practicum.playlist_maker.settings.domain.api.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {
    override fun isDarkTheme(): Boolean {
        return settingsRepository.isDarkTheme()
    }

    override fun switchTheme(isChecked: Boolean) {
        settingsRepository.switchTheme(isChecked)
    }
}