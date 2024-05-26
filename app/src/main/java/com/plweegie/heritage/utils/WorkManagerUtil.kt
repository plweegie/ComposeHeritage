package com.plweegie.heritage.utils

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.plweegie.heritage.work.GeofenceStartWorker

object WorkManagerUtil {

    private const val GEOFENCE_WORK_TAG = "geofence_work"

    fun enqueueWorkRequest(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val request = OneTimeWorkRequestBuilder<GeofenceStartWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            GEOFENCE_WORK_TAG,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }
}