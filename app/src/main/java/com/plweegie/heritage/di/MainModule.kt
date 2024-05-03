package com.plweegie.heritage.di

import com.plweegie.heritage.xmlfeedparser.FeedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object MainModule {

    private const val BASE_URL = "https://www.english-heritage.org.uk/api/"

    @Provides
    fun provideOkHttpClient(cache: Cache): OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    @Provides
    fun provideAPI(retrofit: Retrofit): FeedApi =
        retrofit.create(FeedApi::class.java)
}