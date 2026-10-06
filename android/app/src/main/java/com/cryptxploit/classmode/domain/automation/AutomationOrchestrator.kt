package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.data.local.dao.AutomationEventDao
import com.cryptxploit.classmode.data.local.entity.AutomationEventEntity
import com.cryptxploit.classmode.data.system.SystemAudioController
import com.cryptxploit.classmode.data.system.SystemNotificationManager
import com.cryptxploit.classmode.data.system.SystemHapticController
import com.cryptxploit.classmode.data.preferences.PreferencesManager
import com.cryptxploit.classmode.domain.model.CapabilityResult
import com.cryptxploit.classmode.domain.model.SoundProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.firstOrNull
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
    private val notificationManager: SystemNotificationManager,
    private val hapticController: SystemHapticController,
    private val preferencesManager: PreferencesManager
) {
    fun start(scope: CoroutineScope) {
        contextEngine.observeContext()
            .onEach { snapshot ->
                val resolution = ruleResolver.resolve(snapshot)
                val targetProfile = resolution.profile
                val ruleIdStr = resolution.ruleId
                

                // Update notification status
                if (resolution.source == com.cryptxploit.classmode.domain.model.ResolutionSource.ACTIVE_RULE) {
                    val title = context.getString(R.string.status_automation_active)
                    val message = "Profile set to $targetProfile"
                    notificationManager.showAutomationStatus(title, message)
                } else {
                    notificationManager.cancelAutomationStatus()
                }

                val lastResolvedProfile = preferencesManager.lastResolvedProfileFlow.firstOrNull()
                
                // If the context dictates a change in the intended profile
                if (targetProfile != lastResolvedProfile) {
                    val currentPhysicalProfile = audioController.getCurrentProfile()
                    
                    // Apply only if the physical state differs from the new target
                    if (currentPhysicalProfile != targetProfile) {
                        val success = audioController.applyProfile(targetProfile)
                        if (success) {
                            hapticController.performAutomationTransitionEffect()
                            val ruleId = ruleIdStr?.toLongOrNull() ?: 0L
                            logEvent(ruleId, targetProfile, CapabilityResult.APPLIED)
                        } else {
                            val ruleId = ruleIdStr?.toLongOrNull() ?: 0L
                            logEvent(ruleId, targetProfile, CapabilityResult.DENIED)
                        }
                    }
                    
                    // Always record that we resolved this target, so we don't spam it
                    preferencesManager.setLastResolvedProfile(targetProfile)
                }
            }
            .launchIn(scope)
    }

    private suspend fun logEvent(ruleId: Long, profile: SoundProfile, result: CapabilityResult) {
        val event = AutomationEventEntity(
            ruleId = ruleId,
            timestamp = System.currentTimeMillis(),
            result = result,
            details = "Profile: $profile"
        )
        eventDao.insertEvent(event)
    }
}
