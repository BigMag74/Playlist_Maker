package com.practicum.playlist_maker.settings.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.settings.domain.SettingsInteractor


class SettingsViewModel(private val interactor: SettingsInteractor) : ViewModel() {

    val isDarkTheme = interactor.isDarkTheme()

    fun switchTheme(isChecked: Boolean) = interactor.switchTheme(isChecked)

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]?.let { Creator.provideSettingsInteractor(it) }
                SettingsViewModel(interactor!!)
            }
        }
    }


}