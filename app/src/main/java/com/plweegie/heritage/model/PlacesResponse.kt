package com.plweegie.heritage.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PlacesResponse(

    @Expose
    @SerializedName("PropertyList")
    val placesList: List<HeritagePlace>
)
