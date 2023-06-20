package com.practicum.playlist_maker.search.di

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlist_maker.search.data.NetworkClient
import com.practicum.playlist_maker.search.data.network.ItunesApi
import com.practicum.playlist_maker.search.data.network.SearchRetrofitNetworkClient
import com.practicum.playlist_maker.search.data.sharedPreferences.LocalStorage
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchDataModule = module {

    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("http://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single {
        LocalStorage(
            androidContext().getSharedPreferences(
                "track_list_shared_preferences",
                Context.MODE_PRIVATE
            )
        )
    }

    factory { Gson() }

    single<NetworkClient> { SearchRetrofitNetworkClient(get(), androidContext()) }
}