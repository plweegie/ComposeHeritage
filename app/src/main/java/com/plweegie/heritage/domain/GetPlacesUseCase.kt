package com.plweegie.heritage.domain

import com.plweegie.heritage.model.HeritagePlace
import com.plweegie.heritage.model.PlacesResponse
import kotlinx.coroutines.flow.Flow

class GetPlacesUseCase(private val repository: PlacesRepository) {

    fun getPlacesFeedFlow(): Flow<List<HeritagePlace>> {
        return repository.getPlacesFeedFlow()
    }

    suspend fun getPlacesFeed(): PlacesResponse {
        return repository.getPlacesFeed()
    }
}