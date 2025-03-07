package com.plweegie.heritage.domain

class GetCurrentLocationUseCase(private val repository: PlacesRepository) {

    val currentLocation = repository.currentLocation

    suspend fun findCurrentLocation() {
        repository.findCurrentLocation()
    }
}