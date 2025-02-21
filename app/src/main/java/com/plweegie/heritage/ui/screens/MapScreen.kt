package com.plweegie.heritage.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.LatLng
import com.plweegie.heritage.ui.components.HeritageTopAppBar
import com.plweegie.heritage.ui.components.LoadingIndicator
import com.plweegie.heritage.ui.components.PlacesMap
import com.plweegie.heritage.viewmodel.PlacesMapViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    viewModel: PlacesMapViewModel = viewModel()
) {

    val scope = rememberCoroutineScope()

    val placesListState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HeritageTopAppBar(
                actions = {
                    IconButton(onClick = {
                        scope.launch(Dispatchers.IO) {
                            viewModel.findCurrentLocation()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.MyLocation,
                            contentDescription = "My Location"
                        )
                    }
                }
            )
        }) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingIndicator(placesListState.value is PlacesMapViewModel.UiState.Loading)

            PlacesMap(
                places = placesListState.value.let {
                    if (it is PlacesMapViewModel.UiState.Success) {
                        it.places
                    } else {
                        emptyList()
                    }
                },
                currentLocation = with(viewModel.currentLocation.value) {
                    LatLng(latitude, longitude)
                }
            )
        }
    }
}