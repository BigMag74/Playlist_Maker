package com.practicum.playlist_maker.settings.view_model

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker.creator.Creator
import com.practicum.playlist_maker.search.ui.view_model.SearchViewModel
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