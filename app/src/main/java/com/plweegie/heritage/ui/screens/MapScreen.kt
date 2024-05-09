package com.plweegie.heritage.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plweegie.heritage.R
import com.plweegie.heritage.ui.components.PlacesMap
import com.plweegie.heritage.viewmodel.PlacesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: PlacesListViewModel = viewModel()
) {

    val placesListState = viewModel.uiState.collectAsStateWithLifecycle()

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

        PlacesMap(
            modifier = Modifier.padding(innerPadding),
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