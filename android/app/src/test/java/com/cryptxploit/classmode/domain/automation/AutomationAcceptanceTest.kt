package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.domain.model.ActiveSession
import com.cryptxploit.classmode.domain.model.AutomationRuleCondition
import com.cryptxploit.classmode.domain.model.ContextSnapshot
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Before

class AutomationAcceptanceTest {
    
    private lateinit var resolver: RuleResolver

    @Before
    fun setup() {
        resolver = RuleResolver()
    }

    // CASE 1: No schedule + default NORMAL -> NORMAL
    @Test
    fun `case 1 no schedule and default NORMAL resolves to NORMAL`() {
        val snapshot = ContextSnapshot(
            activeSessions = emptyList(),
            defaultPreference = SoundProfile.NORMAL
        )
        assertEquals(SoundProfile.NORMAL, resolver.resolve(snapshot).profile)
    }

    // CASE 2: No schedule + default VIBRATE -> VIBRATE
    @Test
    fun `case 2 no schedule and default VIBRATE resolves to VIBRATE`() {
        val snapshot = ContextSnapshot(
            activeSessions = emptyList(),
            defaultPreference = SoundProfile.VIBRATE
        )
        assertEquals(SoundProfile.VIBRATE, resolver.resolve(snapshot).profile)
    }

    // CASE 3: Manual override Silent -> SILENT regardless of default
    @Test
    fun `case 3 manual override Silent resolves to SILENT regardless of default or rules`() {
        val snapshot = ContextSnapshot(
            activeSessions = listOf(ActiveSession("1", SessionType.CLASS, false, SoundProfile.VIBRATE)),
            defaultPreference = SoundProfile.NORMAL,
            userOverride = SoundProfile.SILENT
        )
        assertEquals(SoundProfile.SILENT, resolver.resolve(snapshot).profile)
    }

    // CASE 4: Manual override ends while no rule is active -> DEFAULT
    @Test
    fun `case 4 manual override ends while no rule is active resolves to DEFAULT`() {
        // Modeled by context emitting a snapshot without user override
        val snapshot = ContextSnapshot(
            activeSessions = emptyList(),
            defaultPreference = SoundProfile.NORMAL,
            userOverride = null
        )
        assertEquals(SoundProfile.NORMAL, resolver.resolve(snapshot).profile)
    }

    // CASE 5: Manual override ends while class rule is active -> CLASS RULE PROFILE
    @Test
    fun `case 5 manual override ends while class rule active resolves to CLASS RULE PROFILE`() {
        val snapshot = ContextSnapshot(
            activeSessions = listOf(ActiveSession("1", SessionType.CLASS, false, SoundProfile.VIBRATE)),
            defaultPreference = SoundProfile.NORMAL,
            userOverride = null
        )
        assertEquals(SoundProfile.VIBRATE, resolver.resolve(snapshot).profile)
    }

    // CASE 6: Time schedule start -> SELECTED PROFILE
    @Test
    fun `case 6 time schedule active resolves to SELECTED PROFILE`() {
        val snapshot = ContextSnapshot(
            activeSessions = listOf(ActiveSession("1", SessionType.CLASS, false, SoundProfile.SILENT)),
            defaultPreference = SoundProfile.NORMAL
        )
        assertEquals(SoundProfile.SILENT, resolver.resolve(snapshot).profile)
    }

    // CASE 7: Time schedule end -> DEFAULT or next active rule
    @Test
    fun `case 7 time schedule end resolves to DEFAULT or next active rule`() {
        val snapshotNoRules = ContextSnapshot(
            activeSessions = emptyList(),
            defaultPreference = SoundProfile.NORMAL
        )
        assertEquals(SoundProfile.NORMAL, resolver.resolve(snapshotNoRules).profile)
        
        val snapshotNextRule = ContextSnapshot(
            activeSessions = listOf(ActiveSession("2", SessionType.CAMPUS, false, SoundProfile.VIBRATE)),
            defaultPreference = SoundProfile.NORMAL
        )
        assertEquals(SoundProfile.VIBRATE, resolver.resolve(snapshotNextRule).profile)
    }

    // CASE 14: Time + Location both TRUE -> selected profile
    @Test
    fun `case 14 time and location both true resolves to selected profile`() {
        val snapshot = ContextSnapshot(
            activeSessions = listOf(ActiveSession("1", SessionType.CLASS, false, SoundProfile.DND)),
            defaultPreference = SoundProfile.NORMAL
        )
        assertEquals(SoundProfile.DND, resolver.resolve(snapshot).profile)
    }

    // CASE 19: User-selected Silent schedule -> MUST resolve Silent, not hardcoded Vibrate
    @Test
    fun `case 19 user selected Silent schedule resolves to Silent not Vibrate`() {
        val snapshot = ContextSnapshot(
            activeSessions = listOf(ActiveSession("1", SessionType.CLASS, false, SoundProfile.SILENT)),
            defaultPreference = SoundProfile.NORMAL
        )
        assertEquals(SoundProfile.SILENT, resolver.resolve(snapshot).profile)
    }

    // CASE 20: User-selected Normal schedule -> MUST actively resolve Normal
    @Test
    fun `case 20 user selected Normal schedule resolves to Normal`() {
        val snapshot = ContextSnapshot(
            activeSessions = listOf(ActiveSession("1", SessionType.CLASS, false, SoundProfile.NORMAL)),
            defaultPreference = SoundProfile.VIBRATE
        )
        assertEquals(SoundProfile.NORMAL, resolver.resolve(snapshot).profile)
    }

    // CASE 23: Multiple active rules -> deterministic documented precedence
    @Test
    fun `case 23 multiple active rules follows precedence EXAM over CLASS over CAMPUS over DEFAULT`() {
        val snapshot = ContextSnapshot(
            activeSessions = listOf(
                ActiveSession("campus", SessionType.CAMPUS, false, SoundProfile.VIBRATE),
                ActiveSession("class", SessionType.CLASS, false, SoundProfile.SILENT),
                ActiveSession("exam", SessionType.EXAM, false, SoundProfile.DND)
            ),
            defaultPreference = SoundProfile.NORMAL
        )
        assertEquals(SoundProfile.DND, resolver.resolve(snapshot).profile)
    }
}
