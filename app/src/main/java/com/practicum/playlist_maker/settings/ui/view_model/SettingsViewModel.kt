package com.practicum.playlist_maker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import com.practicum.playlist_maker.settings.domain.api.SettingsInteractor


class SettingsViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    val isDarkTheme = interactor.isDarkTheme()

    fun switchTheme(isChecked: Boolean) = interactor.switchTheme(isChecked)

}