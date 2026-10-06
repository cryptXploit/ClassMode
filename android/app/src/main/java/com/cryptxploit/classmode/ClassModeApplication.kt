package com.cryptxploit.classmode

import android.app.Application
import android.content.Context
import com.cryptxploit.classmode.data.local.AppDatabase
import com.cryptxploit.classmode.data.preferences.PreferencesManager
import com.cryptxploit.classmode.data.preferences.RestoreStateRepository
import com.cryptxploit.classmode.data.system.SystemAudioController
import com.cryptxploit.classmode.domain.automation.AutomationHealthMonitor
import com.cryptxploit.classmode.domain.automation.ContextEngine
import com.cryptxploit.classmode.domain.automation.RestoreStateManager
import com.cryptxploit.classmode.domain.automation.RuleResolver
import com.cryptxploit.classmode.domain.automation.AutomationOrchestrator
import com.cryptxploit.classmode.data.system.SystemHapticController
import com.cryptxploit.classmode.data.system.SystemNotificationManager

class ClassModeApplication : Application() {
    
    lateinit var preferencesManager: PreferencesManager
    lateinit var database: AppDatabase
    lateinit var systemAudioController: SystemAudioController
    lateinit var geofenceManager: com.cryptxploit.classmode.data.system.GeofenceManager
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
        geofenceManager = com.cryptxploit.classmode.data.system.GeofenceManager(this)
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
            preferencesManager = preferencesManager,
            notificationManager = systemNotificationManager,
            hapticController = systemHapticController
        )
        orchestrator.start(kotlinx.coroutines.GlobalScope)
    }
}



