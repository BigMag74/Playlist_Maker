package com.practicum.playlist_maker.player.di

import androidx.room.Room
import com.practicum.playlist_maker.player.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val audioPlayerDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}