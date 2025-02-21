package com.plweegie.heritage.utils

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.plweegie.heritage.work.GeofenceStartWorker
import java.util.concurrent.TimeUnit

object WorkManagerUtil {

    private const val GEOFENCE_WORK_TAG = "geofence_work"

    fun enqueueWorkRequest(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val request = PeriodicWorkRequestBuilder<GeofenceStartWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            GEOFENCE_WORK_TAG,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}