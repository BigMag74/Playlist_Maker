package com.practicum.playlist_maker.player.di

import android.media.MediaPlayer
import com.practicum.playlist_maker.player.data.impl.AudioPlayerImpl
import com.practicum.playlist_maker.player.domain.api.AudioPlayer
import org.koin.dsl.module

val audioPlayerModule = module {
    factory<AudioPlayer> { AudioPlayerImpl(MediaPlayer()) }
}