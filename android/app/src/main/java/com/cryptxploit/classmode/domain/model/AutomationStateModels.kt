package com.cryptxploit.classmode.domain.model

data class CapturedState(
    val profile: SoundProfile,
    val capturedAtMillis: Long,
    val ringerMode: Int,
    val zenMode: Int
)

data class ActiveAutomationTarget(
    val sessionId: String,
    val profile: SoundProfile,
    val appliedAtMillis: Long
)

enum class CapabilityResult {
    PENDING, APPLIED, DENIED, FAILED, UNSUPPORTED, UNKNOWN, RESTORED
}

data class AutomationEvent(
    val eventId: Long = 0,
    val ruleId: Long,
    val timestamp: Long,
    val result: CapabilityResult,
    val details: String
)

data class HealthStatus(
    val lastAttemptMillis: Long = 0L,
    val lastSuccessfulOperation: String = "None",
    val isHealthy: Boolean = true,
    val lastError: String? = null
)

data class CapabilityStatus(
    val hasNotificationPolicyAccess: Boolean,
    val hasExactAlarmAccess: Boolean,
    val hasBackgroundLocationAccess: Boolean,
    val hasPostNotificationsAccess: Boolean
)

