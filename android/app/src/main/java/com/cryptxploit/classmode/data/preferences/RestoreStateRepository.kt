package com.cryptxploit.classmode.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.cryptxploit.classmode.domain.model.ActiveAutomationTarget
import com.cryptxploit.classmode.domain.model.CapturedState
import com.cryptxploit.classmode.domain.model.SoundProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RestoreStateRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("restore_state_prefs", Context.MODE_PRIVATE)
    
    private val _originalState = MutableStateFlow<CapturedState?>(loadOriginalState())
    private val _activeTarget = MutableStateFlow<ActiveAutomationTarget?>(loadActiveTarget())

    val originalStateFlow: StateFlow<CapturedState?> = _originalState.asStateFlow()
    val activeTargetFlow: StateFlow<ActiveAutomationTarget?> = _activeTarget.asStateFlow()

    fun saveOriginalState(profile: SoundProfile, ringerMode: Int = 2, zenMode: Int = 0) {
        val state = CapturedState(profile, System.currentTimeMillis(), ringerMode, zenMode)
        _originalState.value = state
        prefs.edit()
            .putString("orig_profile", profile.name)
            .putLong("orig_time", state.capturedAtMillis)
            .putInt("orig_ringer", ringerMode)
            .putInt("orig_zen", zenMode)
            .apply()
    }

    fun saveActiveTarget(sessionId: String, profile: SoundProfile) {
        val target = ActiveAutomationTarget(sessionId, profile, System.currentTimeMillis())
        _activeTarget.value = target
        prefs.edit()
            .putString("target_session", sessionId)
            .putString("target_profile", profile.name)
            .putLong("target_time", target.appliedAtMillis)
            .apply()
    }

    fun clearState() {
        _originalState.value = null
        _activeTarget.value = null
        prefs.edit().clear().apply()
    }
    
    private fun loadOriginalState(): CapturedState? {
        val profileStr = prefs.getString("orig_profile", null) ?: return null
        val time = prefs.getLong("orig_time", 0L)
        val ringer = prefs.getInt("orig_ringer", 2)
        val zen = prefs.getInt("orig_zen", 0)
        return try {
            CapturedState(SoundProfile.valueOf(profileStr), time, ringer, zen)
        } catch (e: Exception) {
            null
        }
    }
    
    private fun loadActiveTarget(): ActiveAutomationTarget? {
        val session = prefs.getString("target_session", null) ?: return null
        val profileStr = prefs.getString("target_profile", null) ?: return null
        val time = prefs.getLong("target_time", 0L)
        return try {
            ActiveAutomationTarget(session, SoundProfile.valueOf(profileStr), time)
        } catch (e: Exception) {
            null
        }
    }
}


