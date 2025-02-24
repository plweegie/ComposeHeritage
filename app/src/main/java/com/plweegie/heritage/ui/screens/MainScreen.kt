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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plweegie.heritage.R
import com.plweegie.heritage.ui.components.HeritageDropdownMenu
import com.plweegie.heritage.ui.components.HeritageTopAppBar
import com.plweegie.heritage.ui.components.LoadingIndicator
import com.plweegie.heritage.ui.components.PlacesList
import com.plweegie.heritage.viewmodel.PlacesListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: PlacesListViewModel = viewModel(),
    onNavigateToDetails: (placeName: String) -> Unit
) {

    val scope = rememberCoroutineScope()

    val regions = listOf(
        "",
        "East Midlands",
        "East of England",
        "London",
        "North East",
        "North West",
        "South East",
        "South West",
        "West Midlands",
        "Yorkshire and the Humber"
    )

    val placeCategories = listOf(
        "",
        "Abbeys and churches",
        "Castles and forts",
        "Gardens",
        "Houses and palaces",
        "Medieval and tudor",
        "Prehistoric",
        "Roman"
    )

    val placesListState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = Modifier.fillMaxSize(),
        topBar = {
            HeritageTopAppBar(
                actions = {
                    IconButton(onClick = {
                        scope.launch(Dispatchers.IO) {
                            viewModel.findCurrentLocation()
                            viewModel.sortedByDistance = true
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
            HeritageDropdownMenu(
                label = stringResource(id = R.string.category),
                options = placeCategories,
                onSelected = { viewModel.categoryFilter = it }
            )

            HeritageDropdownMenu(
                label = stringResource(id = R.string.region),
                options = regions,
                onSelected = { viewModel.regionFilter = it }
            )

            LoadingIndicator(placesListState is PlacesListViewModel.UiState.Loading)

            PlacesList(
                onItemClicked = onNavigateToDetails,
                places = placesListState.let {
                    if (it is PlacesListViewModel.UiState.Success) {
                        it.places
                    } else {
                        emptyList()
                    }
                }
            )
        }
    }
}