package com.practicum.playlist_maker.settings.di

import com.practicum.playlist_maker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsViewModelModule = module {

    viewModel { SettingsViewModel(get()) }
}