package com.plweegie.heritage.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.plweegie.heritage.LocationTracker
import com.plweegie.heritage.R
import com.plweegie.heritage.model.HeritagePlace
import kotlinx.coroutines.delay

@Composable
fun PlacesMap(
    modifier: Modifier = Modifier,
    places: List<HeritagePlace>,
    currentLocation: LatLng
) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation, 7f)
    }

    val vectorMarker = ContextCompat.getDrawable(
        LocalContext.current,
        R.drawable.marker_castle
    )?.apply {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    }

    val vectorBitmap = vectorMarker?.toBitmap()

    LaunchedEffect(currentLocation) {
        delay(timeMillis = 500)

        if (currentLocation.latitude != LocationTracker.DEFAULT_LOCATION.latitude &&
            currentLocation.longitude != LocationTracker.DEFAULT_LOCATION.longitude) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.Builder().target(currentLocation).zoom(9f).build()
                ),
                durationMs = 500
            )
        }
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        val bitmapDescriptor = vectorBitmap?.let {
            BitmapDescriptorFactory.fromBitmap(it)
        } ?: BitmapDescriptorFactory.defaultMarker()

        places.forEach {
            val position = LatLng(it.latitude, it.longitude)
            Marker(
                icon = bitmapDescriptor,
                state = MarkerState(position = position),
                title = it.title,
                onClick = {
                    it.showInfoWindow()
                    true
                }
            )
        }
    }
}