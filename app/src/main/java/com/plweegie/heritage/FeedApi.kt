package com.plweegie.heritage

import com.plweegie.heritage.model.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface FeedApi {
    @GET("PropertyFeed/GetAll/")
    suspend fun getPlacesFeed(): PlacesResponse
}