package com.classmode.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.classmode.data.preferences.PreferencesManager
import com.classmode.data.system.SystemAudioController
import com.classmode.domain.model.SoundProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesManager: PreferencesManager,
    private val systemAudioController: SystemAudioController,
    private val appDatabase: com.classmode.data.local.AppDatabase
) : ViewModel() {
    val isHapticsEnabled = preferencesManager.isHapticsEnabledFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    val isAutomationEnabled = preferencesManager.isAutomationEnabledFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )
    
    val themeMode = preferencesManager.themeModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "System"
    )

    val language = preferencesManager.languageFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "System"
    )

    val defaultProfile = preferencesManager.defaultProfileFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SoundProfile.NORMAL
    )

    private val _testResult = MutableStateFlow<String?>(null)
    val testResult = _testResult.asStateFlow()

    fun toggleAutomation(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setAutomationEnabled(enabled)
        }
    }

    fun toggleHaptics(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setHapticsEnabled(enabled)
        }
    }

    fun setThemeMode(mode: String) {
        viewModelScope.launch {
            preferencesManager.setThemeMode(mode)
        }
    }

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            preferencesManager.setLanguage(lang)
        }
    }

    fun clearAllData() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            appDatabase.clearAllTables()
            preferencesManager.clearAllData()
        }
    }

    fun setDefaultProfile(profile: SoundProfile) {
        viewModelScope.launch {
            preferencesManager.setDefaultProfile(profile)
        }
    }

    fun testSoundProfile(profile: SoundProfile) {
        val success = systemAudioController.applyProfile(profile)
        if (success) {
            val verified = systemAudioController.getCurrentProfile()
            if (verified == profile) {
                _testResult.value = "APPLIED: $verified"
            } else {
                _testResult.value = "FAILED: Requested $profile but got $verified"
            }
        } else {
            _testResult.value = "DENIED: Missing permissions for $profile"
        }
    }
}

