package com.plweegie.heritage.di

import com.google.ai.client.generativeai.GenerativeModel
import com.plweegie.heritage.BuildConfig
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
}