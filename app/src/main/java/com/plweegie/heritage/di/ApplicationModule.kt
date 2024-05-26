package com.plweegie.heritage.di

import android.app.Application
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.plweegie.heritage.location.GeofenceManager
import com.plweegie.heritage.location.LocationTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideCache(app: Application): Cache {
        val cacheSize = 5 * 1024 * 1024
        return Cache(app.cacheDir, cacheSize.toLong())
    }

    @Provides
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun provideLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        @ApplicationContext context: Context
    ) = LocationTracker(fusedLocationProviderClient, context)

    @Provides
    fun provideGeofencingClient(@ApplicationContext context: Context): GeofencingClient =
        LocationServices.getGeofencingClient(context)

    @Provides
    fun provideGeofenceManager(
        geofencingClient: GeofencingClient,
        @ApplicationContext context: Context
    ): GeofenceManager = GeofenceManager(geofencingClient, context)
}