package com.classmode.domain.automation

import com.classmode.data.preferences.RestoreStateRepository
import com.classmode.data.system.SystemAudioController
import com.classmode.domain.model.SoundProfile

/**
 * Handles safe transitions between sound profiles.
 * Implements the requirement: "Never blindly switch the device to Normal after every session."
 */
class RestoreStateManager(
    private val audioController: SystemAudioController,
    private val stateRepository: RestoreStateRepository,
    private val healthMonitor: AutomationHealthMonitor
) {
    /**
     * Called when a new schedule or automation rule becomes active.
     */
    suspend fun captureAndApply(sessionId: String, targetProfile: SoundProfile) {
        val currentPhysicalProfile = audioController.getCurrentProfile()
        
        // Prevent overwriting the original state if we are transitioning between overlapping automations
        val existingTarget = stateRepository.activeTargetFlow.value
        if (existingTarget == null) {
            stateRepository.saveOriginalState(currentPhysicalProfile)
        }

        val success = audioController.applyProfile(targetProfile)
        if (success) {
            stateRepository.saveActiveTarget(sessionId, targetProfile)
            healthMonitor.logSuccess("Applied $targetProfile for session $sessionId")
        } else {
            healthMonitor.logFailure("Failed to apply $targetProfile: Missing DND/System permissions")
        }
    }

    /**
     * Called when a schedule or automation rule ends.
     */
    suspend fun evaluateAndRestore(sessionId: String) {
        val activeTarget = stateRepository.activeTargetFlow.value
        
        // If the ending session isn't the currently active automation, ignore it.
        if (activeTarget == null || activeTarget.sessionId != sessionId) {
            return
        }

        val currentPhysicalProfile = audioController.getCurrentProfile()
        
        // INTELLIGENCE CHECK: Did the user manually change their volume while the class/focus was active?
        if (currentPhysicalProfile != activeTarget.profile) {
            healthMonitor.logWarning("Manual override detected. Aborting restore to respect user choice.")
            stateRepository.clearState()
            return
        }

        // It is safe to restore
        val originalState = stateRepository.originalStateFlow.value
        if (originalState != null) {
            val success = audioController.applyProfile(originalState.profile)
            if (success) {
                healthMonitor.logSuccess("Restored ${originalState.profile} after session $sessionId")
            } else {
                healthMonitor.logFailure("Failed to restore ${originalState.profile}")
            }
        }
        
        stateRepository.clearState()
    }
}
