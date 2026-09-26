package com.classmode.domain.automation

import com.classmode.domain.model.HealthStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Tracks the success, failure, and permission restrictions of all automation attempts.
 * Feeds directly into the local diagnostic screen.
 */
class AutomationHealthMonitor {
    private val _healthStatus = MutableStateFlow(HealthStatus())
    val healthStatus: StateFlow<HealthStatus> = _healthStatus.asStateFlow()

    fun logSuccess(operation: String) {
        _healthStatus.value = _healthStatus.value.copy(
            lastAttemptMillis = System.currentTimeMillis(),
            lastSuccessfulOperation = operation,
            isHealthy = true,
            lastError = null
        )
    }

    fun logFailure(reason: String) {
        _healthStatus.value = _healthStatus.value.copy(
            lastAttemptMillis = System.currentTimeMillis(),
            isHealthy = false,
            lastError = reason
        )
    }

    fun logWarning(warning: String) {
        // Warnings do not fail the overall health but are logged for diagnostics
        _healthStatus.value = _healthStatus.value.copy(
            lastError = "Warning: $warning"
        )
    }
}
