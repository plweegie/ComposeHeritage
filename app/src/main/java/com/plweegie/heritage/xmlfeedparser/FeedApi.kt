package com.plweegie.heritage.xmlfeedparser

import retrofit2.http.GET
import retrofit2.http.Headers

interface FeedApi {

    @Headers("Accept: application/xml")
    @GET("PropertyFeed/GetAll/")
    suspend fun getPlacesFeed(): String
}