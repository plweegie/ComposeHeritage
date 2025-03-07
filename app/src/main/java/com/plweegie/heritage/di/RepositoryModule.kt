package com.plweegie.heritage.di

import com.plweegie.heritage.domain.PlacesRepository
import com.plweegie.heritage.model.PlacesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(impl: PlacesRepositoryImpl): PlacesRepository
}