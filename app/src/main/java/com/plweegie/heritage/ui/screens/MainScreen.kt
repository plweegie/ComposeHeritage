package com.plweegie.heritage.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plweegie.heritage.R
import com.plweegie.heritage.ui.components.HeritageDropdownMenu
import com.plweegie.heritage.ui.components.LoadingIndicator
import com.plweegie.heritage.ui.components.PlacesList
import com.plweegie.heritage.viewmodel.PlacesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: PlacesListViewModel = viewModel()
) {

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

    val placesListState = viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = { Text(text = stringResource(id = R.string.main_title).uppercase()) },
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

            LoadingIndicator(placesListState.value is PlacesListViewModel.UiState.Loading)

            PlacesList(
                places = placesListState.value.let {
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