package com.cryptxploit.classmode.domain.model

data class EffectiveResolution(
    val profile: SoundProfile,
    val source: ResolutionSource,
    val ruleId: String?,
    val reason: String
)

enum class ResolutionSource {
    MANUAL_OVERRIDE,
    ACTIVE_RULE,
    DEFAULT_PREFERENCE
}
