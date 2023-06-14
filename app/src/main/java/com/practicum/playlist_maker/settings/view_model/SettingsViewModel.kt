package com.practicum.playlist_maker.settings.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlist_maker.App
import com.practicum.playlist_maker.creator.Creator.provideSettingsInteractor
import com.practicum.playlist_maker.search.ui.SearchState
import com.practicum.playlist_maker.search.ui.view_model.SearchViewModel
import com.practicum.playlist_maker.settings.data.SettingsRepositoryImpl
import com.practicum.playlist_maker.settings.domain.SettingsInteractor
import com.practicum.playlist_maker.settings.domain.SettingsRepository
import com.practicum.playlist_maker.settings.domain.impl.SettingsInteractorImpl

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val interactor: SettingsInteractor = provideSettingsInteractor(getApplication())

    val isDarkTheme = interactor.isDarkTheme()

    fun switchTheme(isChecked: Boolean) = interactor.switchTheme(isChecked)


    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }


}