package com.example.android.devbyteviewer.di

import androidx.room.Room
import com.example.android.devbyteviewer.database.VideoDao
import com.example.android.devbyteviewer.database.VideosDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dbModule = module {
    single<VideosDatabase> {
        Room.databaseBuilder(androidContext(),
                VideosDatabase::class.java,
                "videos")
                .build()
    }
    single<VideoDao> { get<VideosDatabase>().videoDao }
}