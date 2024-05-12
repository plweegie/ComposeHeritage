package com.plweegie.heritage

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.tasks.await

class LocationTracker(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val applicationContext: Context
) {

    companion object {
        val DEFAULT_LOCATION = Location(LocationManager.NETWORK_PROVIDER).apply {
            latitude = 52.56
            longitude = -1.47
        }
    }

    suspend fun getCurrentLocation(): Location {
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = applicationContext.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled && !(hasAccessCoarseLocationPermission || hasAccessFineLocationPermission)) {
            return DEFAULT_LOCATION
        }

        return try {
            fusedLocationProviderClient.lastLocation.await() ?: DEFAULT_LOCATION
        } catch (e: Exception) {
            DEFAULT_LOCATION
        }
    }
}