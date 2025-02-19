package com.plweegie.heritage.location

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest

class GeofenceManager(
    private val geofencingClient: GeofencingClient,
    private val applicationContext: Context
) {

    private companion object {
        const val GEOFENCE_RADIUS_METERS = 100f
        const val GEOFENCE_DURATION_MILLIS = 3600 * 1000L // 1 hour
    }

    private val geofencePendingIntent by lazy {
        val intent = Intent(applicationContext, GeofencingBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private val geofences = mutableListOf<Geofence>()

    private val geofencingRequest = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
        .addGeofences(geofences)
        .build()

    fun addGeofence(
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
        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).run {
            addOnSuccessListener {
                Log.d("GeofenceManager", "Geofences added")
            }

            addOnFailureListener { e ->
                Log.e("GeofenceManager", "Failed to add geofences", e)
            }
        }
    }
}