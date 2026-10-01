package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.data.local.dao.AutomationEventDao
import com.cryptxploit.classmode.data.local.entity.AutomationEventEntity
import com.cryptxploit.classmode.data.system.SystemAudioController
import com.cryptxploit.classmode.data.system.SystemNotificationManager
import com.cryptxploit.classmode.data.system.SystemHapticController
import com.cryptxploit.classmode.domain.model.CapabilityResult
import com.cryptxploit.classmode.domain.model.SoundProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context
import com.cryptxploit.classmode.R

class AutomationOrchestrator(
    private val context: Context,
    private val contextEngine: ContextEngine,
    private val ruleResolver: IRuleResolver,
    private val audioController: SystemAudioController,
    private val eventDao: AutomationEventDao,
    private val restoreStateManager: RestoreStateManager,
    private val notificationManager: SystemNotificationManager,
    private val hapticController: SystemHapticController
) {
    private var lastActiveSessionId: String? = null
    private var isManualOverrideActive: Boolean = false
    private var lastDefaultPreference: SoundProfile? = null

    fun start(scope: CoroutineScope) {
        contextEngine.observeContext()
            .onEach { snapshot ->
                val targetProfile = ruleResolver.resolve(snapshot)
                val activeSession = snapshot.activeSessions.firstOrNull()
                val currentSessionId = activeSession?.id
                val hasUserOverride = snapshot.userOverride != null
                
                val defaultPreferenceChanged = lastDefaultPreference != null && lastDefaultPreference != snapshot.defaultPreference
                lastDefaultPreference = snapshot.defaultPreference
                
                if (hasUserOverride) {
                    val currentPhysicalProfile = audioController.getCurrentProfile()
                    if (currentPhysicalProfile != targetProfile) {
                        audioController.applyProfile(targetProfile)
                        hapticController.performAutomationTransitionEffect()
                    }
                    isManualOverrideActive = true
                    return@onEach
                }
                
                if (isManualOverrideActive && !hasUserOverride) {
                    isManualOverrideActive = false
                }

                if (currentSessionId != null) {
                    if (currentSessionId != lastActiveSessionId) {
                        // New session started or transitioned
                        restoreStateManager.captureAndApply(currentSessionId, targetProfile)
                        logEvent(activeSession.id.toLongOrNull() ?: 0L, targetProfile, CapabilityResult.APPLIED)
                        lastActiveSessionId = currentSessionId
                        
                        // Show notification
                        val title = context.getString(R.string.status_automation_active)
                        val message = "Profile set to $targetProfile"
                        notificationManager.showAutomationStatus(title, message)
                        hapticController.performAutomationTransitionEffect()
                    } else {
                        // Ongoing session
                        val currentPhysicalProfile = audioController.getCurrentProfile()
                        if (currentPhysicalProfile != targetProfile && currentPhysicalProfile == SoundProfile.NORMAL) {
                            // User manually changed physical volume button - respect it
                        }
                    }
                } else {
                    // No active session
                    if (lastActiveSessionId != null) {
                        // Session ended
                        restoreStateManager.evaluateAndRestore(lastActiveSessionId!!)
                        logEvent(0L, SoundProfile.NORMAL, CapabilityResult.RESTORED)
                        lastActiveSessionId = null
                        
                        // Cancel automation notification
                        notificationManager.cancelAutomationStatus()
                        hapticController.performAutomationTransitionEffect()
                    } else if (defaultPreferenceChanged) {
                        // Dynamically propagate default profile change if no class is running
                        val currentPhysicalProfile = audioController.getCurrentProfile()
                        if (currentPhysicalProfile != targetProfile) {
                            audioController.applyProfile(targetProfile)
                            hapticController.performAutomationTransitionEffect()
                        }
                    }
                }
            }
            .launchIn(scope)
    }

    private suspend fun logEvent(ruleId: Long, profile: SoundProfile, result: CapabilityResult) = withContext(Dispatchers.IO) {
        val event = AutomationEventEntity(
            ruleId = ruleId,
            timestamp = System.currentTimeMillis(),
            result = result,
            details = "Profile: $profile"
        )
        eventDao.insertEvent(event)
    }
}




