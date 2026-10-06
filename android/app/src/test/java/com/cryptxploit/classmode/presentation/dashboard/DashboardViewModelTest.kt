package com.cryptxploit.classmode.presentation.dashboard

import com.cryptxploit.classmode.data.preferences.PreferencesManager
import com.cryptxploit.classmode.data.system.SystemAlarmScheduler
import com.cryptxploit.classmode.data.system.SystemHapticController
import com.cryptxploit.classmode.domain.automation.AutomationHealthMonitor
import com.cryptxploit.classmode.domain.automation.ContextEngine
import com.cryptxploit.classmode.domain.automation.RuleResolver
import com.cryptxploit.classmode.domain.model.ContextSnapshot
import com.cryptxploit.classmode.domain.model.EffectiveResolution
import com.cryptxploit.classmode.domain.model.ResolutionSource
import com.cryptxploit.classmode.domain.model.SoundProfile
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var contextEngine: ContextEngine
    private lateinit var ruleResolver: RuleResolver
    private lateinit var healthMonitor: AutomationHealthMonitor
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var systemAlarmScheduler: SystemAlarmScheduler
    private lateinit var hapticController: SystemHapticController

    private lateinit var viewModel: DashboardViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        contextEngine = mockk(relaxed = true)
        ruleResolver = mockk(relaxed = true)
        healthMonitor = mockk(relaxed = true)
        preferencesManager = mockk(relaxed = true)
        systemAlarmScheduler = mockk(relaxed = true)
        hapticController = mockk(relaxed = true)

        val mockContextSnapshot = ContextSnapshot(
            activeSessions = emptyList(),
            userOverride = null,
            defaultPreference = SoundProfile.NORMAL,
            isSystemDndActive = false,
            isLocationUnavailable = false
        )

        val mockContextFlow = MutableStateFlow(mockContextSnapshot)
        every { contextEngine.observeContext() } returns mockContextFlow

        val mockResolution = EffectiveResolution(
            profile = SoundProfile.SILENT,
            source = ResolutionSource.ACTIVE_RULE,
            ruleId = "123",
            reason = "Test rule"
        )
        every { ruleResolver.resolve(any()) } returns mockResolution

        viewModel = DashboardViewModel(
            contextEngine, ruleResolver, healthMonitor, preferencesManager, systemAlarmScheduler, hapticController
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `effectiveResolution exposes exact domain engine state`() = runTest {
        val job = launch { viewModel.effectiveResolution.collect {} }`n        advanceUntilIdle()
        val resolution = viewModel.effectiveResolution.value
        assertEquals(SoundProfile.SILENT, resolution.profile)
        assertEquals(ResolutionSource.ACTIVE_RULE, resolution.source)
        job.cancel()
    }
}
