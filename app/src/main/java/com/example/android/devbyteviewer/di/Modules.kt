package com.example.android.devbyteviewer.di

import android.app.Application
import androidx.room.Room
import com.example.android.devbyteviewer.database.VideoDao
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.network.DevbyteService
import com.example.android.devbyteviewer.repository.VideosRepository
import com.example.android.devbyteviewer.viewmodels.DevByteViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val dbModule = module {
    single<VideosDatabase> {
        Room.databaseBuilder(androidContext(),
                VideosDatabase::class.java, "videos")
                .fallbackToDestructiveMigration()
                .build()
    }
    single<VideoDao> { get<VideosDatabase>().videoDao }
}

val networkModule = module {
    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     */
    factory<Moshi> {
        Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
                .baseUrl("https://devbytes.udacity.com/")
                .addConverterFactory(MoshiConverterFactory.create(get<Moshi>()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
    }

    /**
     * Main entry point for network access.
     */
    single<DevbyteService> {
        get<Retrofit>().create(DevbyteService::class.java)
    }
}

val viewModelModule = module {
    viewModel { (application: Application) -> DevByteViewModel(application) }
}

val repositoryModule = module {
    factory<VideosRepository> { VideosRepository(get(), get()) }
}