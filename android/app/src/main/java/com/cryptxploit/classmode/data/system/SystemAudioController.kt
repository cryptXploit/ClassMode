package com.cryptxploit.classmode.data.system

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import android.os.Build
import com.cryptxploit.classmode.data.system.SystemNotificationManager
import com.cryptxploit.classmode.domain.model.SoundProfile

/**
 * Handles actual device sound profile changes safely.
 * Never claims success unless the Android system accepts the change.
 */
class SystemAudioController(private val context: Context, private val healthMonitor: com.cryptxploit.classmode.domain.automation.AutomationHealthMonitor) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * Checks if the app has permission to change Do Not Disturb (DND) settings.
     * Required for SILENT and DND modes on modern Android versions.
     */
    fun hasDndPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notificationManager.isNotificationPolicyAccessGranted
        } else {
            true // Pre-Marshmallow does not strictly require this for ringer changes
        }
    }

    /**
     * Attempts to apply the sound profile. 
     * Returns true if successful, false if restricted by permissions.
     */
    fun applyProfile(profile: SoundProfile): Boolean {
        if ((profile == SoundProfile.DND || profile == SoundProfile.SILENT) && !hasDndPermission()) {
            healthMonitor.logFailure("Do Not Disturb permission required")
            SystemNotificationManager(context).showProblemNotification("Permission Required", "ClassMode needs Do Not Disturb permission.")
            return false // Honest platform behavior: fail gracefully if permission is missing
        }

        try {
            when (profile) {
                SoundProfile.NORMAL -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                    }
                    audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                }
                SoundProfile.VIBRATE -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                    }
                    audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
                }
                SoundProfile.SILENT -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
                    }
                    audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
                }
                SoundProfile.DND -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
                    } else {
                        audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
                    }
                }
            }
            return true
        } catch (e: SecurityException) {
            healthMonitor.logFailure("SecurityException while changing sound profile")
            return false
        }
    }

    /**
     * Reads the current physical device state.
     */
    fun getCurrentProfile(): SoundProfile {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val filter = notificationManager.currentInterruptionFilter
            if (filter == NotificationManager.INTERRUPTION_FILTER_NONE || filter == NotificationManager.INTERRUPTION_FILTER_PRIORITY || filter == NotificationManager.INTERRUPTION_FILTER_ALARMS) {
                return SoundProfile.DND
            }
        }
        
        return when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> SoundProfile.NORMAL
            AudioManager.RINGER_MODE_VIBRATE -> SoundProfile.VIBRATE
            AudioManager.RINGER_MODE_SILENT -> SoundProfile.SILENT
            else -> SoundProfile.NORMAL
        }
    }
}


