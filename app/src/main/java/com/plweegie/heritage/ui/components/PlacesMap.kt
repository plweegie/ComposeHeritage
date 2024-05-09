package com.plweegie.heritage.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.plweegie.heritage.R
import com.plweegie.heritage.model.HeritagePlace

@Composable
fun PlacesMap(
    modifier: Modifier = Modifier,
    places: List<HeritagePlace>
) {
    val center = LatLng(52.56, -1.47)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 7f)
    }

    val vectorMarker = ContextCompat.getDrawable(
        LocalContext.current,
        R.drawable.marker_castle
    )?.apply {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
    }

    val vectorBitmap = vectorMarker?.toBitmap()

    GoogleMap(
        modifier = modifier,
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