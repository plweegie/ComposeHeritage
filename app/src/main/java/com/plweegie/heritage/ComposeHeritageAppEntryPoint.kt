package com.plweegie.heritage

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.plweegie.heritage.ui.navigation.AppScreen
import com.plweegie.heritage.ui.navigation.MainScreenNavHost
import com.plweegie.heritage.ui.screens.MapScreen
import com.plweegie.heritage.ui.theme.ComposeHeritageTheme
import com.plweegie.heritage.viewmodel.PlaceDetailViewModel
import com.plweegie.heritage.viewmodel.PlacesListViewModel
import com.plweegie.heritage.viewmodel.PlacesMapViewModel

@Composable
fun ComposeHeritageAppEntryPoint() {
    val navController = rememberNavController()
    val viewModel = hiltViewModel<PlacesListViewModel>()
    val mapViewModel = hiltViewModel<PlacesMapViewModel>()
    val detailsViewModel = hiltViewModel<PlaceDetailViewModel>()

    val navigationItems = listOf(
        AppScreen.Main,
        AppScreen.Map
    )

    ComposeHeritageTheme {

        Scaffold(
            contentWindowInsets = WindowInsets(0.dp),
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    navigationItems.forEach { screen ->
                        NavigationBarItem(
                            colors = NavigationBarItemDefaults.colors().copy(
                                selectedIconColor = MaterialTheme.colorScheme.tertiary,
                                selectedTextColor = MaterialTheme.colorScheme.tertiary,
                                selectedIndicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f)
                            ),
                            icon = { Icon(screen.icon, contentDescription = screen.route) },
                            label = { Text(stringResource(id = screen.resourceId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                navController = navController,
                startDestination = AppScreen.Main.route
            ) {
                composable(AppScreen.Main.route) {
                    MainScreenNavHost(
                        listViewModel = viewModel,
                        detailViewModel = detailsViewModel
                    )
                }
                composable(AppScreen.Map.route) {
                    MapScreen(viewModel = mapViewModel)
                }
            }
        }
    }
}