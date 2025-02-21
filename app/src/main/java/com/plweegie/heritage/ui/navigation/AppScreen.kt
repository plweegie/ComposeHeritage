package com.plweegie.heritage.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.graphics.vector.ImageVector
import com.plweegie.heritage.R

sealed class AppScreen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    data object Main : AppScreen("main", R.string.list_label, Icons.AutoMirrored.Filled.List)
    data object Map : AppScreen("map", R.string.map_label, Icons.Filled.Map)
}