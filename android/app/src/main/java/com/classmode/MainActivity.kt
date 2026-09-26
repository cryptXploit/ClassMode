package com.classmode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import com.classmode.presentation.dashboard.DashboardViewModel
import com.classmode.presentation.schedules.ScheduleViewModel
import com.classmode.presentation.settings.SettingsViewModel
import com.classmode.presentation.alarms.AlarmViewModel
import com.classmode.data.repository.AlarmRepository
import com.classmode.presentation.location.LocationViewModel
import com.classmode.presentation.focus.FocusViewModel
import com.classmode.presentation.theme.ClassModeTheme
import com.classmode.presentation.main.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manual Dependency Injection Factory
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val app = application as ClassModeApplication
                return when {
                    modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                        @Suppress("UNCHECKED_CAST")
                        DashboardViewModel(
                            contextEngine = app.contextEngine,
                            ruleResolver = app.ruleResolver,
                            healthMonitor = app.healthMonitor,
                            preferencesManager = app.preferencesManager,
                            hapticController = app.systemHapticController
                        ) as T
                    }
                    modelClass.isAssignableFrom(ScheduleViewModel::class.java) -> {
                        @Suppress("UNCHECKED_CAST")
                        ScheduleViewModel(app.database.scheduleDao(), com.classmode.data.system.SystemAlarmScheduler(applicationContext, app.healthMonitor), app.database.geofenceDao(), app.geofenceManager) as T
                    }
                    modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                        @Suppress("UNCHECKED_CAST")
                        SettingsViewModel(app.preferencesManager, app.systemAudioController, app.database) as T
                    }
                    modelClass.isAssignableFrom(LocationViewModel::class.java) -> {
                        @Suppress("UNCHECKED_CAST")
                        LocationViewModel(app.database.geofenceDao(), app.geofenceManager) as T
                    }
                                        modelClass.isAssignableFrom(AlarmViewModel::class.java) -> {
                        @Suppress("UNCHECKED_CAST")
                        AlarmViewModel(AlarmRepository(app.database.alarmDao()), com.classmode.data.system.SystemAlarmScheduler(applicationContext, app.healthMonitor)) as T
                    }
                    modelClass.isAssignableFrom(FocusViewModel::class.java) -> {
                        @Suppress("UNCHECKED_CAST")
                        FocusViewModel(app.database.scheduleDao(), com.classmode.data.system.SystemAlarmScheduler(applicationContext, app.healthMonitor)) as T
                    }
                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }

        setContent {
            val settingsViewModel: SettingsViewModel = viewModel(factory = factory)
            val themeMode by settingsViewModel.themeMode.collectAsStateWithLifecycle()
            val darkTheme = when (themeMode) {
                "Dark" -> true
                "Light" -> false
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }

            val language by settingsViewModel.language.collectAsStateWithLifecycle()
            LaunchedEffect(language) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    val localeList = if (language == "Bengali") android.os.LocaleList(java.util.Locale("bn"))
                    else if (language == "English") android.os.LocaleList(java.util.Locale("en"))
                    else android.os.LocaleList.getEmptyLocaleList()
                    getSystemService(android.app.LocaleManager::class.java).applicationLocales = localeList
                } else {
                    val locale = if (language == "Bengali") java.util.Locale("bn") else java.util.Locale("en")
                    java.util.Locale.setDefault(locale)
                    val config = android.content.res.Configuration()
                    config.setLocale(locale)
                    @Suppress("DEPRECATION")
                    baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
                }
            }

            ClassModeTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val dashboardViewModel: DashboardViewModel = viewModel(factory = factory)
                    val scheduleViewModel: ScheduleViewModel = viewModel(factory = factory)
                    val locationViewModel: LocationViewModel = viewModel(factory = factory)
                    val alarmViewModel: AlarmViewModel = viewModel(factory = factory)
                    val focusViewModel: FocusViewModel = viewModel(factory = factory)
                    
                    MainScreen(
                        dashboardViewModel = dashboardViewModel,
                        scheduleViewModel = scheduleViewModel,
                        settingsViewModel = settingsViewModel,
                        alarmViewModel = alarmViewModel,
                        locationViewModel = locationViewModel,
                        focusViewModel = focusViewModel
                    )
                }
            }
        }
    }
}




