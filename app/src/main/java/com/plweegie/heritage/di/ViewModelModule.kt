package com.plweegie.heritage.di

import com.google.ai.client.generativeai.GenerativeModel
import com.plweegie.heritage.BuildConfig
import com.plweegie.heritage.domain.GeneratePlaceDescriptionUseCase
import com.plweegie.heritage.domain.GetCurrentLocationUseCase
import com.plweegie.heritage.domain.GetPlacesUseCase
import com.plweegie.heritage.domain.PlacesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    private const val MODEL_NAME = "gemini-1.5-flash"

    @Provides
    fun provideGenerativeModel(): GenerativeModel = GenerativeModel(
        modelName = MODEL_NAME,
        apiKey = BuildConfig.apiKey
    )

    @Provides
    fun providePlacesUseCase(repository: PlacesRepository): GetPlacesUseCase =
        GetPlacesUseCase(repository)

    @Provides
    fun provideLocationUseCase(repository: PlacesRepository): GetCurrentLocationUseCase =
        GetCurrentLocationUseCase(repository)

    @Provides
    fun providePlaceDescriptionUseCase(generativeModel: GenerativeModel): GeneratePlaceDescriptionUseCase =
        GeneratePlaceDescriptionUseCase(generativeModel)
}