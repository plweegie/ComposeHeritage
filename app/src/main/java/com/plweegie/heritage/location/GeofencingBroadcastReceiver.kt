package com.plweegie.heritage.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.plweegie.heritage.R
import kotlin.random.Random

class GeofencingBroadcastReceiver : BroadcastReceiver() {

    private companion object {
        const val CHANNEL_ID = "com.plweegie.heritage"
        const val TAG = "GeofencingBroadcastReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        Log.d(TAG, "Geofencing event received: ${geofencingEvent.triggeringGeofences?.first()?.requestId}")

        val geofenceTransition = geofencingEvent.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            triggeringGeofences?.first()?.let {
                sendNotification(context, it)
            }
        }
    }

    private fun sendNotification(context: Context, geofence: Geofence) {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = createNotificationChannel()

        notificationManager.createNotificationChannel(notificationChannel)

        val notification = createNotification(context, geofence.requestId)
        notificationManager.notify(Random.nextInt(), notification)
    }

    private fun createNotification(context: Context, placeName: String): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setTicker(context.getString(R.string.geofence_title))
            .setContentTitle(context.getString(R.string.geofence_title))
            .setContentText(context.getString(R.string.geofence_text, placeName))
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
    }

    private fun createNotificationChannel(): NotificationChannel {
        val name = "English Heritage"
        val descriptionText = "Nearby property notifications"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        return channel
    }
}