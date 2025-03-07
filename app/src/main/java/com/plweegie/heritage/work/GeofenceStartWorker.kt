package com.plweegie.heritage.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.plweegie.heritage.domain.PlacesRepository
import com.plweegie.heritage.location.GeofenceManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class GeofenceStartWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val geofenceManager: GeofenceManager,
    private val repository: PlacesRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        repository.findCurrentLocation()

        val places = repository.getPlacesFeed().placesList.sortedBy {
            repository.currentLocation.value.distanceTo(it.location)
        }.take(5)

        geofenceManager.apply {
            addGeofences(places)
            startMonitoringGeofences()
        }

        return Result.success()
    }
}