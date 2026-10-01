package com.classmode.data.system

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Handles subtle haptic feedback for user interactions and critical automation events.
 * Respects system availability.
 */
import com.classmode.data.preferences.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SystemHapticController(context: Context, preferencesManager: PreferencesManager) {
    private var isHapticsEnabled = true
    
    init {
        CoroutineScope(Dispatchers.IO).launch {
            preferencesManager.isHapticsEnabledFlow.collect { enabled ->
                isHapticsEnabled = enabled
            }
        }
    }

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    /**
     * Subtle tap feedback for toggles and manual overrides.
     */
    fun performClickEffect() {
        if (!isHapticsEnabled || vibrator == null || !vibrator.hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(20L)
        }
    }

    /**
     * Distinct pattern for when a class or focus session starts/ends automatically.
     */
    fun performAutomationTransitionEffect() {
        if (!isHapticsEnabled || vibrator == null || !vibrator.hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timings = longArrayOf(0, 50, 100, 50)
            val amplitudes = intArrayOf(0, 100, 0, 150)
            vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 50, 100, 50), -1)
        }
    }
}

