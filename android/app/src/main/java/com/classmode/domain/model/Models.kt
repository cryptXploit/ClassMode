package com.classmode.domain.model

enum class SoundProfile {
    NORMAL, VIBRATE, SILENT, DND
}

enum class SessionType {
    SLEEP, EXAM, CLASS, FOCUS, CAMPUS, MANUAL_OVERRIDE
}

data class ActiveSession(
    val id: String,
    val type: SessionType,
    val isUserInitiated: Boolean,
    val soundProfile: SoundProfile
)

data class ContextSnapshot(
    val activeSessions: List<ActiveSession>,
    val userOverride: SoundProfile? = null,
    val defaultPreference: SoundProfile = SoundProfile.NORMAL,
    val isSystemDndActive: Boolean = false,
    val isLocationUnavailable: Boolean = false
)

enum class AutomationRuleCondition {
    TIME_ONLY, LOCATION_ONLY, TIME_AND_LOCATION, TIME_OR_LOCATION
}

data class AutomationRule(
    val id: Long,
    val type: SessionType,
    val title: String,
    val soundProfile: SoundProfile,
    val condition: AutomationRuleCondition,
    val isEnabled: Boolean
)

data class ScheduleDomainModel(
    val ruleId: Long,
    val title: String,
    val startTimeMins: Int,
    val endTimeMins: Int,
    val daysOfWeek: Int,
    val soundProfile: SoundProfile,
    val isEnabled: Boolean
)

data class ClassroomGeofence(
    val ruleId: Long,
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Float
)

data class AlarmDomainModel(
    val id: Long,
    val timeMins: Int,
    val daysOfWeek: Int,
    val isEnabled: Boolean,
    val isVibrationEnabled: Boolean,
    val snoozeMins: Int,
    val label: String
)
