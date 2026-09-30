package com.classmode.presentation.main

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
import com.classmode.presentation.others.OthersScreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.classmode.presentation.dashboard.DashboardScreen
import com.classmode.presentation.dashboard.DashboardViewModel
import com.classmode.presentation.schedules.SchedulesScreen
import com.classmode.presentation.schedules.ScheduleViewModel
import com.classmode.presentation.settings.SettingsScreen
import com.classmode.presentation.settings.SettingsViewModel
import com.classmode.presentation.alarms.AlarmsScreen
import com.classmode.presentation.alarms.AlarmViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object Schedules : Screen("schedules", "Schedules", Icons.Default.List)
    object Location : Screen("location", "Locations", Icons.Default.LocationOn)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object Alarms : Screen("alarms", "Alarms", Icons.Default.Notifications)
    object Focus : Screen("focus", "Focus", Icons.Default.Lock)
    object Others : Screen("others", "Others", Icons.Default.Menu)
}

@Composable
fun MainScreen(
    alarmViewModel: AlarmViewModel,
    dashboardViewModel: DashboardViewModel,
    scheduleViewModel: ScheduleViewModel,
    settingsViewModel: SettingsViewModel,
    locationViewModel: com.classmode.presentation.location.LocationViewModel,
    focusViewModel: com.classmode.presentation.focus.FocusViewModel
) {
    val navController = rememberNavController()
    val screens = listOf(Screen.Dashboard, Screen.Schedules, Screen.Location, Screen.Others, Screen.Settings)

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
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true || (screen == Screen.Others && (currentRoute == Screen.Focus.route || currentRoute == Screen.Alarms.route))
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = screen.title,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        label = { Text(screen.title, color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) },
                        selected = isSelected,
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
                composable(Screen.Focus.route) { com.classmode.presentation.focus.FocusScreen(viewModel = focusViewModel) }
                composable(Screen.Alarms.route) { AlarmsScreen(viewModel = alarmViewModel) }
                composable(Screen.Location.route) { 
                    val geofences by locationViewModel.geofences.collectAsState(initial = emptyList())
                    com.classmode.presentation.location.LocationScreen(geofences = geofences, onAddGeofence = { lat, lon, rad -> locationViewModel.addGeofence(lat, lon, rad) }, onDeleteGeofence = { locationViewModel.deleteGeofence(it) }) 
                }
            }
        }
    }
}



