package com.cryptxploit.classmode.presentation.main

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cryptxploit.classmode.R
import com.cryptxploit.classmode.presentation.alarms.AlarmViewModel
import com.cryptxploit.classmode.presentation.alarms.AlarmsScreen
import com.cryptxploit.classmode.presentation.dashboard.DashboardScreen
import com.cryptxploit.classmode.presentation.dashboard.DashboardViewModel
import com.cryptxploit.classmode.presentation.focus.FocusScreen
import com.cryptxploit.classmode.presentation.focus.FocusViewModel
import com.cryptxploit.classmode.presentation.location.LocationScreen
import com.cryptxploit.classmode.presentation.location.LocationViewModel
import com.cryptxploit.classmode.presentation.others.OthersScreen
import com.cryptxploit.classmode.presentation.schedules.ScheduleViewModel
import com.cryptxploit.classmode.presentation.schedules.SchedulesScreen
import com.cryptxploit.classmode.presentation.settings.SettingsScreen
import com.cryptxploit.classmode.presentation.settings.SettingsViewModel
import com.cryptxploit.classmode.presentation.theme.LocalHaptic

sealed class Screen(val route: String, val titleRes: Int, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", R.string.title_dashboard, Icons.Default.Home)
    object Schedules : Screen("schedules", R.string.title_schedules, Icons.Default.List)
    object Location : Screen("location", R.string.title_locations, Icons.Default.LocationOn)
    object Settings : Screen("settings", R.string.title_settings, Icons.Default.Settings)
    object Alarms : Screen("alarms", R.string.title_custom_alarms, Icons.Default.Notifications)
    object Focus : Screen("focus", R.string.title_focus_mode, Icons.Default.Lock)
    object Others : Screen("others", R.string.nav_others, Icons.Default.Menu)
}

@Composable
fun MainScreen(
    alarmViewModel: AlarmViewModel,
    dashboardViewModel: DashboardViewModel,
    scheduleViewModel: ScheduleViewModel,
    settingsViewModel: SettingsViewModel,
    locationViewModel: LocationViewModel,
    focusViewModel: FocusViewModel
) {
    val navController = rememberNavController()
    val screens = listOf(Screen.Dashboard, Screen.Schedules, Screen.Location, Screen.Others, Screen.Settings)
    val haptic = LocalHaptic.current

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    val currentRoute = currentDestination?.route
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true || 
                                     (screen == Screen.Others && (currentRoute == Screen.Focus.route || currentRoute == Screen.Alarms.route))
                    
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = stringResource(screen.titleRes)
                            )
                        },
                        label = { Text(stringResource(screen.titleRes)) },
                        selected = isSelected,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        onClick = {
                            if (!isSelected) haptic.performClickEffect()
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
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.Dashboard.route,
                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, animationSpec = tween(300)) },
                popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) },
                popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, animationSpec = tween(300)) }
            ) {
                composable(Screen.Dashboard.route) { DashboardScreen(viewModel = dashboardViewModel) }
                composable(Screen.Schedules.route) { SchedulesScreen(viewModel = scheduleViewModel) }
                composable(Screen.Settings.route) { SettingsScreen(viewModel = settingsViewModel) }
                composable(Screen.Others.route) { OthersScreen(navController = navController) }
                composable(Screen.Focus.route) { FocusScreen(viewModel = focusViewModel, navController = navController) }
                composable(Screen.Alarms.route) { AlarmsScreen(viewModel = alarmViewModel) }
                composable(Screen.Location.route) { 
                    val geofences by locationViewModel.geofences.collectAsState(initial = emptyList())
                    LocationScreen(
                        geofences = geofences, 
                        onAddGeofence = { lat, lon, rad -> locationViewModel.addGeofence(lat, lon, rad) }, 
                        onDeleteGeofence = { locationViewModel.deleteGeofence(it) }
                    ) 
                }
            }
        }
    }
}
