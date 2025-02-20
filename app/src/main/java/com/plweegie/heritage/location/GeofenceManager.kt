package com.plweegie.heritage.location

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.plweegie.heritage.model.HeritagePlace

class GeofenceManager(
    private val geofencingClient: GeofencingClient,
    private val applicationContext: Context
) {

    private companion object {
        const val GEOFENCE_RADIUS_METERS = 2000f
        const val GEOFENCE_TEST_RADIUS_METERS = 200f
        const val GEOFENCE_DURATION_MILLIS = 3600 * 1000L // 1 hour
        const val GEOFENCE_TEST_DURATION_MILLIS = 6 * 3600 * 1000L // 6 hours
    }

    private val geofencePendingIntent by lazy {
        val intent = Intent(applicationContext, GeofencingBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val geofences = mutableListOf<Geofence>()

    private val geofencingRequestBuilder = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)

    fun addTestGeofence() {
        geofences.clear()

        geofences.add(
            Geofence.Builder()
                .setExpirationDuration(GEOFENCE_TEST_DURATION_MILLIS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
        )
    }

    fun addGeofences(places: List<HeritagePlace>) {
        geofences.clear()

        places.forEach { place ->
            addGeofence(
                place.title,
                place.location.latitude,
                place.location.longitude
            )
        }
    }

    private fun addGeofence(
        placeName: String,
        latitude: Double,
        longitude: Double
    ) {
        geofences.add(
            Geofence.Builder()
                .setRequestId(placeName)
                .setCircularRegion(latitude, longitude, GEOFENCE_RADIUS_METERS)
                .setExpirationDuration(GEOFENCE_DURATION_MILLIS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                .build()
        )
    }

    @SuppressLint("MissingPermission")
    fun startMonitoringGeofences() {
        val geofencingRequest = geofencingRequestBuilder.addGeofences(geofences).build()

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
            addOnSuccessListener {
                Log.d(
                    "GeofenceManager",
                    "Geofences added: total of ${geofencingRequest.geofences.size}, first is ${geofencingRequest.geofences.first()}"
                )
            }

            addOnFailureListener { e ->
                Log.e("GeofenceManager", "Failed to add geofences", e)
            }
        }
    }
}