package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.domain.model.ContextSnapshot
import com.cryptxploit.classmode.domain.model.EffectiveResolution
import com.cryptxploit.classmode.domain.model.ResolutionSource
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile

interface IRuleResolver {
    fun resolve(context: ContextSnapshot): EffectiveResolution
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

    override fun resolve(context: ContextSnapshot): EffectiveResolution {
        // 1. Explicit user overrides take absolute precedence
        if (context.userOverride != null) {
            return EffectiveResolution(
                profile = context.userOverride,
                source = ResolutionSource.MANUAL_OVERRIDE,
                ruleId = null,
                reason = "Manual override active"
            )
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
            val matchingSessions = context.activeSessions.filter { it.type == type }
            if (matchingSessions.isNotEmpty()) {
                // Explicit tie-breaking: If multiple rules of the exact same priority are active,
                // sort them by ID (stable string sort) to ensure deterministic outcomes.
                val tieBrokenSession = matchingSessions.sortedBy { it.id }.first()
                
                return EffectiveResolution(
                    profile = tieBrokenSession.requestedProfile,
                    source = ResolutionSource.ACTIVE_RULE,
                    ruleId = tieBrokenSession.id,
                    reason = "Priority $type rule active"
                )
            }
        }

        // Fallback to user's default preference
        return EffectiveResolution(
            profile = context.defaultPreference,
            source = ResolutionSource.DEFAULT_PREFERENCE,
            ruleId = null,
            reason = "Default preference fallback"
        )
    }
}
