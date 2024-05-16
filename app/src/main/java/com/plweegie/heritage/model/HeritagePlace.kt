package com.plweegie.heritage.model

import android.location.Location
import android.location.LocationManager.NETWORK_PROVIDER
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HeritagePlace(

    @Expose
    @SerializedName("ID")
    val id: Int,

    @Expose
    @SerializedName("Title")
    val title: String,

    @Expose
    @SerializedName("Description")
    val description: String,

    @Expose
    @SerializedName("Path")
    val path: String,

    @Expose
    @SerializedName("ImagePath")
    val imagePath: String,

    @Expose
    @SerializedName("Category")
    val category: String,

    @Expose
    @SerializedName("IsFreeEntry")
    val isFreeEntry: Boolean,

    @Expose
    @SerializedName("Latitude")
    val latitude: Double,

    @Expose
    @SerializedName("Longitude")
    val longitude: Double,

    @Expose
    @SerializedName("County")
    val county: String,

    @Expose
    @SerializedName("Region")
    val region: String
) {
    val location: Location
        get() {
            val loc = Location(NETWORK_PROVIDER).also {
                it.latitude = latitude
                it.longitude = longitude
            }

            return loc
        }
}
