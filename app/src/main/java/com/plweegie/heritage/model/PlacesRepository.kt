package com.plweegie.heritage.model

import androidx.compose.runtime.mutableStateOf
import com.plweegie.heritage.FeedApi
import com.plweegie.heritage.LocationTracker

class PlacesRepository(
    private val placesFeedApi: FeedApi,
    private val locationTracker: LocationTracker,
) {
    val currentLocation = mutableStateOf(LocationTracker.DEFAULT_LOCATION)

    suspend fun findCurrentLocation() {
        currentLocation.value = locationTracker.getCurrentLocation()
    }

    suspend fun getPlacesFeed(): PlacesResponse = placesFeedApi.getPlacesFeed()
}