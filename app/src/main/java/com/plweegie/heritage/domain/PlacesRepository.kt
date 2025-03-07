package com.plweegie.heritage.domain

import android.location.Location
import androidx.compose.runtime.MutableState
import com.plweegie.heritage.model.HeritagePlace
import com.plweegie.heritage.model.PlacesResponse
import kotlinx.coroutines.flow.Flow

interface PlacesRepository {
    val currentLocation: MutableState<Location>
    suspend fun findCurrentLocation()
    suspend fun getPlacesFeed(): PlacesResponse
    fun getPlacesFeedFlow(): Flow<List<HeritagePlace>>
}