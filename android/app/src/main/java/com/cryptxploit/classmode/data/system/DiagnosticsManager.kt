package com.cryptxploit.classmode.data.system

import com.cryptxploit.classmode.data.preferences.PreferencesManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Wraps Firebase Crashlytics to enforce our privacy-first mandate.
 * Crash reporting is completely disabled at the SDK level until explicit user consent is granted.
 */
class DiagnosticsManager(
    private val preferencesManager: PreferencesManager,
    private val appScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    fun initialize() {
        appScope.launch {
            // Collect the opt-in preference as a Flow. 
            // If the user revokes consent, data collection stops immediately.
            preferencesManager.isDiagnosticsOptInFlow.collect { isOptedIn ->
                FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(isOptedIn)
            }
        }
    }

    /**
     * Logs non-fatal exceptions safely.
     * Firebase SDK will automatically drop this if collection is disabled.
     */
    fun logNonFatal(exception: Exception, contextData: Map<String, String> = emptyMap()) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        contextData.forEach { (key, value) ->
            crashlytics.setCustomKey(key, value)
        }
        crashlytics.recordException(exception)
    }

    /**
     * Used by the AutomationHealthMonitor to record silent restrictions (like OEM background limits).
     */
    fun logPlatformRestriction(reason: String) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.setCustomKey("restriction_type", reason)
        crashlytics.log("Platform restriction encountered: $reason")
    }
}

