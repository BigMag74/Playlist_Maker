package com.practicum.playlist_maker.creator

import android.content.Context
import com.practicum.playlist_maker.SETTINGS_PREFERENCES
import com.practicum.playlist_maker.player.data.impl.AudioPlayerImpl
import com.practicum.playlist_maker.player.domain.api.AudioPlayer
import com.practicum.playlist_maker.search.data.TracksRepositoryImpl
import com.practicum.playlist_maker.search.data.network.SearchRetrofitNetworkClient
import com.practicum.playlist_maker.search.data.sharedPreferences.LocalStorage
import com.practicum.playlist_maker.search.domain.api.TracksInteractor
import com.practicum.playlist_maker.search.domain.api.TracksRepository
import com.practicum.playlist_maker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlist_maker.settings.data.SettingsRepositoryImpl
import com.practicum.playlist_maker.settings.domain.SettingsInteractor
import com.practicum.playlist_maker.settings.domain.SettingsRepository
import com.practicum.playlist_maker.settings.domain.impl.SettingsInteractorImpl

object Creator {

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            SearchRetrofitNetworkClient(context),
            LocalStorage(
                context.getSharedPreferences(
                    "track_list_shared_preferences",
                    Context.MODE_PRIVATE
                )
            )
        )
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            context.getSharedPreferences(
                SETTINGS_PREFERENCES,
                Context.MODE_PRIVATE
            )
        )
    }

    fun provideAudioPlayer(): AudioPlayer {
        return AudioPlayerImpl()
    }
}