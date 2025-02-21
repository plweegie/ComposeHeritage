package com.plweegie.heritage.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.plweegie.heritage.ui.screens.MainScreen
import com.plweegie.heritage.ui.screens.PlaceDetailScreen
import com.plweegie.heritage.viewmodel.PlaceDetailViewModel
import com.plweegie.heritage.viewmodel.PlacesListViewModel
import kotlinx.serialization.Serializable

@Serializable
object ListDestination
@Serializable
data class DetailDestination(val placeName: String)

@Composable
fun MainScreenNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    listViewModel: PlacesListViewModel,
    detailViewModel: PlaceDetailViewModel
) {

    NavHost(
        navController = navController,
        startDestination = ListDestination,
        modifier = modifier
    ) {
        composable<ListDestination> {
            MainScreen(
                onNavigateToDetails = {
                    navController.navigate(route = DetailDestination(it))
                },
                viewModel = listViewModel
            )
        }

        composable<DetailDestination> {
            PlaceDetailScreen(
                placeDetails = it.toRoute(),
                viewModel = detailViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}