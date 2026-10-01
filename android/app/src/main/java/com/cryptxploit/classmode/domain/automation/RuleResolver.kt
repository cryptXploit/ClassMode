package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.domain.model.ContextSnapshot
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile

interface IRuleResolver {
    fun resolve(context: ContextSnapshot): SoundProfile
}

/**
 * Deterministic local intelligence engine.
 * Resolves the correct sound profile based on a strict hierarchy:
 * 1. User Override
 * 2. Sleep / Protected Quiet Session
 * 3. Exam
 * 4. Class
 * 5. Focus
 * 6. Campus / Geofence
 * 7. Default Preference
 */
class RuleResolver : IRuleResolver {

    override fun resolve(context: ContextSnapshot): SoundProfile {
        // 1. Explicit user overrides take absolute precedence
        if (context.userOverride != null) {
            return context.userOverride
        }

        val typePriority = listOf(
            SessionType.SLEEP,
            SessionType.EXAM,
            SessionType.CLASS,
            SessionType.FOCUS,
            SessionType.CAMPUS,
            SessionType.MANUAL_OVERRIDE
        )

        // Find the active session that matches the highest priority type
        for (type in typePriority) {
            val matchingSession = context.activeSessions.firstOrNull { it.type == type }
            if (matchingSession != null) {
                return matchingSession.requestedProfile
            }
        }

        // Fallback to user's default preference
        return context.defaultPreference
    }
}

