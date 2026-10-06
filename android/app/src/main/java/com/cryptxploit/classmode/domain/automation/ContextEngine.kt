package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.data.local.dao.ScheduleDao
import com.cryptxploit.classmode.data.local.dao.TriggerStateDao
import com.cryptxploit.classmode.data.preferences.PreferencesManager
import com.cryptxploit.classmode.data.system.SystemAudioController
import com.cryptxploit.classmode.data.system.GeofenceManager
import com.cryptxploit.classmode.domain.model.ActiveSession
import com.cryptxploit.classmode.domain.model.ContextSnapshot
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import com.cryptxploit.classmode.domain.model.AutomationRuleCondition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ContextEngine(
    private val preferencesManager: PreferencesManager,
    private val scheduleDao: ScheduleDao,
    private val triggerStateDao: TriggerStateDao,
    private val systemAudioController: SystemAudioController,
    private val geofenceManager: GeofenceManager
) {
    fun observeContext(): Flow<ContextSnapshot> {
        return combine(
            preferencesManager.defaultProfileFlow,
            preferencesManager.userOverrideFlow,
            scheduleDao.getActiveSchedules(),
            triggerStateDao.observeAllStates()
        ) { defaultProfile, overrideProfile, enabledSchedules, triggerStates ->
            
            val currentTime = System.currentTimeMillis()
            val stateMap = triggerStates.associateBy { it.ruleId }
            val hasLocation = geofenceManager.hasLocationPermission()
            
            val activeSessions = enabledSchedules.mapNotNull { schedule ->
                // Do not short-circuit just because TriggerStateEntity doesn't exist yet.
                // TriggerStateEntity only acts as a caching mechanism for location and an event trigger.
                val state = stateMap[schedule.id]
                
                val isLocationActive = if (hasLocation) state?.isLocationActive == true else false
                val isTimeActive = ScheduleCalculator.isCurrentlyActive(schedule, currentTime)
                
                val isEffectivelyActive = when (schedule.condition) {
                    AutomationRuleCondition.TIME_ONLY -> isTimeActive
                    AutomationRuleCondition.LOCATION_ONLY -> isLocationActive
                    AutomationRuleCondition.TIME_AND_LOCATION -> isTimeActive && isLocationActive
                    AutomationRuleCondition.TIME_OR_LOCATION -> isTimeActive || isLocationActive
                }
                
                if (isEffectivelyActive) {
                    ActiveSession(
                        id = schedule.id.toString(),
                        type = schedule.type,
                        isUserInitiated = false,
                        requestedProfile = schedule.soundProfile
                    )
                } else {
                    null
                }
            }
            
            // Check if any enabled schedule requires location
            val requiresLocation = enabledSchedules.any {
                it.condition == AutomationRuleCondition.LOCATION_ONLY || 
                it.condition == AutomationRuleCondition.TIME_AND_LOCATION ||
                it.condition == AutomationRuleCondition.TIME_OR_LOCATION
            }
            val isLocationUnavailable = requiresLocation && !hasLocation

            ContextSnapshot(
                activeSessions = activeSessions,
                userOverride = overrideProfile,
                defaultPreference = defaultProfile,
                isSystemDndActive = systemAudioController.getCurrentProfile() == SoundProfile.DND,
                isLocationUnavailable = isLocationUnavailable
            )
        }
    }
}
