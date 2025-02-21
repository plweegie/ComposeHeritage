package com.plweegie.heritage.ui.components

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.plweegie.heritage.utils.WorkManagerUtil

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequest(
    applicationContext: Context
) {

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect("key1") {
        locationPermissions.launchMultiplePermissionRequest()
    }

    if (locationPermissions.allPermissionsGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val backgroundLocationPermission = rememberPermissionState(
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        LaunchedEffect("key2") {
            backgroundLocationPermission.launchPermissionRequest()
        }

        if (backgroundLocationPermission.status.isGranted) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val notificationPermission = rememberPermissionState(
                    android.Manifest.permission.POST_NOTIFICATIONS
                )

                LaunchedEffect("key3") {
                    notificationPermission.launchPermissionRequest()
                }
            }

            LaunchedEffect("key4") {
                WorkManagerUtil.enqueueWorkRequest(applicationContext)
            }
        }
    }
}