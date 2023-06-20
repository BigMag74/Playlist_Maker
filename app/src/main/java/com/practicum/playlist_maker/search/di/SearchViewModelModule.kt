package com.practicum.playlist_maker.search.di

import com.practicum.playlist_maker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }
}