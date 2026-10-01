package com.classmode

import android.app.Application
import android.content.Context
import com.classmode.data.local.AppDatabase
import com.classmode.data.preferences.PreferencesManager
import com.classmode.data.preferences.RestoreStateRepository
import com.classmode.data.system.SystemAudioController
import com.classmode.domain.automation.AutomationHealthMonitor
import com.classmode.domain.automation.ContextEngine
import com.classmode.domain.automation.RestoreStateManager
import com.classmode.domain.automation.RuleResolver
import com.classmode.domain.automation.AutomationOrchestrator
import com.classmode.data.system.SystemHapticController
import com.classmode.data.system.SystemNotificationManager

class ClassModeApplication : Application() {
    
    lateinit var preferencesManager: PreferencesManager
    lateinit var database: AppDatabase
    lateinit var systemAudioController: SystemAudioController
    lateinit var geofenceManager: com.classmode.data.system.GeofenceManager
    lateinit var restoreStateRepository: RestoreStateRepository
    lateinit var systemNotificationManager: SystemNotificationManager
    lateinit var systemHapticController: SystemHapticController
    
    lateinit var contextEngine: ContextEngine
    lateinit var ruleResolver: RuleResolver
    lateinit var healthMonitor: AutomationHealthMonitor
    lateinit var restoreStateManager: RestoreStateManager
    lateinit var orchestrator: AutomationOrchestrator

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Google Mobile Ads SDK
        com.google.android.gms.ads.MobileAds.initialize(this)

        
        // Initialize Core Dependencies
        healthMonitor = AutomationHealthMonitor()
        preferencesManager = PreferencesManager(this)
        database = AppDatabase.getDatabase(this)
        systemAudioController = SystemAudioController(this, healthMonitor)
        geofenceManager = com.classmode.data.system.GeofenceManager(this)
        restoreStateRepository = RestoreStateRepository(this)
        systemNotificationManager = SystemNotificationManager(this)
        systemHapticController = SystemHapticController(this, preferencesManager)
        
        // Initialize Domain/Automation Logic
        contextEngine = ContextEngine(
            preferencesManager = preferencesManager,
            scheduleDao = database.scheduleDao(),
            triggerStateDao = database.triggerStateDao(),
            systemAudioController = systemAudioController,
            geofenceManager = geofenceManager
        )
        
        ruleResolver = RuleResolver()
        
        restoreStateManager = RestoreStateManager(
            audioController = systemAudioController,
            stateRepository = restoreStateRepository,
            healthMonitor = healthMonitor
        )
        
        orchestrator = AutomationOrchestrator(
            context = this,
            contextEngine = contextEngine,
            ruleResolver = ruleResolver,
            audioController = systemAudioController,
            eventDao = database.automationEventDao(),
            restoreStateManager = restoreStateManager,
            notificationManager = systemNotificationManager,
            hapticController = systemHapticController
        )
        orchestrator.start(kotlinx.coroutines.GlobalScope)
    }
}


