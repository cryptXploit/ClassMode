package com.cryptxploit.classmode.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptxploit.classmode.domain.automation.AutomationHealthMonitor
import com.cryptxploit.classmode.domain.automation.ContextEngine
import com.cryptxploit.classmode.domain.automation.RuleResolver
import com.cryptxploit.classmode.domain.model.ContextSnapshot
import com.cryptxploit.classmode.domain.model.HealthStatus
import com.cryptxploit.classmode.domain.model.SoundProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.cryptxploit.classmode.data.system.SystemHapticController

import com.cryptxploit.classmode.data.system.SystemAlarmScheduler

class DashboardViewModel(
    private val contextEngine: ContextEngine,
    private val ruleResolver: RuleResolver,
    private val healthMonitor: AutomationHealthMonitor,
    private val preferencesManager: com.cryptxploit.classmode.data.preferences.PreferencesManager,
    private val systemAlarmScheduler: SystemAlarmScheduler,
    private val hapticController: SystemHapticController
) : ViewModel() {

    // Emits the intelligently resolved sound profile based on context
    val effectiveProfile: StateFlow<SoundProfile> = contextEngine.observeContext()
        .map { snapshot -> ruleResolver.resolve(snapshot).profile }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SoundProfile.NORMAL
        )

    val contextSnapshot: StateFlow<ContextSnapshot?> = contextEngine.observeContext()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val healthStatus: StateFlow<HealthStatus> = healthMonitor.healthStatus
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HealthStatus()
        )

    fun setTemporaryOverride(profile: SoundProfile, durationMillis: Long = 0L) {
        viewModelScope.launch {
            hapticController.performClickEffect()
            val expiryTime = if (durationMillis > 0) System.currentTimeMillis() + durationMillis else 0L
            preferencesManager.setUserOverride(profile, expiryTime)
            if (expiryTime > 0) {
                systemAlarmScheduler.scheduleOverrideClear(expiryTime)
            } else {
                systemAlarmScheduler.cancelOverrideClear()
            }
        }
    }

    fun clearOverride() {
        viewModelScope.launch {
            hapticController.performClickEffect()
            preferencesManager.setUserOverride(null)
            systemAlarmScheduler.cancelOverrideClear()
        }
    }
}




