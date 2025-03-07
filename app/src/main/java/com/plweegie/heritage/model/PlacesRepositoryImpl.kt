package com.plweegie.heritage.model

import androidx.compose.runtime.mutableStateOf
import com.plweegie.heritage.FeedApi
import com.plweegie.heritage.domain.PlacesRepository
import com.plweegie.heritage.location.LocationTracker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlacesRepositoryImpl(
    private val placesFeedApi: FeedApi,
    private val locationTracker: LocationTracker,
) : PlacesRepository {
    override val currentLocation = mutableStateOf(LocationTracker.DEFAULT_LOCATION)

    override suspend fun findCurrentLocation() {
        currentLocation.value = locationTracker.getCurrentLocation()
    }

    override suspend fun getPlacesFeed(): PlacesResponse = placesFeedApi.getPlacesFeed()

    override fun getPlacesFeedFlow(): Flow<List<HeritagePlace>> = flow {
        emit(getPlacesFeed().placesList)
    }
}