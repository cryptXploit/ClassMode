package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.domain.model.ActiveSession
import com.cryptxploit.classmode.domain.model.ContextSnapshot
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import org.junit.Assert.assertEquals
import org.junit.Test

class RuleResolverTest {

    private val resolver = RuleResolver()

    @Test
    fun `user override takes absolute priority over everything`() {
        val snapshot = ContextSnapshot(
            activeSessions = listOf(ActiveSession("1", SessionType.EXAM, false, SoundProfile.SILENT)),
            userOverride = SoundProfile.NORMAL
        )
        val result = resolver.resolve(snapshot).profile
        assertEquals(SoundProfile.NORMAL, result)
    }

    @Test
    fun `exam profile overrides class profile when overlapping`() {
        val snapshot = ContextSnapshot(
            activeSessions = listOf(
                ActiveSession("1", SessionType.CLASS, false, SoundProfile.VIBRATE),
                ActiveSession("2", SessionType.EXAM, false, SoundProfile.SILENT)
            )
        )
        val result = resolver.resolve(snapshot).profile
        assertEquals(SoundProfile.SILENT, result)
    }

    @Test
    fun `returns default preference when no rules match`() {
        val snapshot = ContextSnapshot(
            activeSessions = emptyList(),
            defaultPreference = SoundProfile.NORMAL
        )
        val result = resolver.resolve(snapshot).profile
        assertEquals(SoundProfile.NORMAL, result)
    }
}

