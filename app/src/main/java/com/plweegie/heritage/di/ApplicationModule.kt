package com.plweegie.heritage.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideCache(app: Application): Cache {
        val cacheSize = 5 * 1024 * 1024
        return Cache(app.cacheDir, cacheSize.toLong())
    }
}