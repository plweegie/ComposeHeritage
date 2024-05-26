package com.plweegie.heritage.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.plweegie.heritage.location.GeofenceManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GeofenceStartWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val geofenceManager: GeofenceManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        geofenceManager.startMonitoringGeofences()
        return Result.success()
    }
}