package com.plweegie.heritage.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.plweegie.heritage.FeedApi
import com.plweegie.heritage.LocationTracker
import com.plweegie.heritage.model.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
object MainModule {

    private const val BASE_URL = "https://www.english-heritage.org.uk/api/"

    @Provides
    fun provideGson(): Gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()

    @Provides
    fun provideOkHttpClient(cache: Cache): OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .build()

    @Provides
    fun provideRetrofit(gson: Gson, client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    fun provideAPI(retrofit: Retrofit): FeedApi =
        retrofit.create(FeedApi::class.java)

    @Provides
    fun provideRepository(
        api: FeedApi,
        locationTracker: LocationTracker
    ): PlacesRepository = PlacesRepository(api, locationTracker)
}