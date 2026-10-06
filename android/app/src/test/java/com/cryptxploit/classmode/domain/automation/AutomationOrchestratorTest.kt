package com.cryptxploit.classmode.domain.automation

import android.content.Context
import com.cryptxploit.classmode.data.local.dao.AutomationEventDao
import com.cryptxploit.classmode.data.preferences.PreferencesManager
import com.cryptxploit.classmode.data.system.SystemAudioController
import com.cryptxploit.classmode.data.system.SystemHapticController
import com.cryptxploit.classmode.data.system.SystemNotificationManager
import com.cryptxploit.classmode.domain.model.ContextSnapshot
import com.cryptxploit.classmode.domain.model.EffectiveResolution
import com.cryptxploit.classmode.domain.model.ResolutionSource
import com.cryptxploit.classmode.domain.model.SoundProfile
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AutomationOrchestratorTest {

    private lateinit var context: Context
    private lateinit var contextEngine: ContextEngine
    private lateinit var ruleResolver: IRuleResolver
    private lateinit var audioController: SystemAudioController
    private lateinit var eventDao: AutomationEventDao
    private lateinit var notificationManager: SystemNotificationManager
    private lateinit var hapticController: SystemHapticController
    private lateinit var preferencesManager: PreferencesManager
    
    private lateinit var orchestrator: AutomationOrchestrator
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val contextFlow = MutableStateFlow<ContextSnapshot?>(null)
    private val lastResolvedFlow = MutableStateFlow<SoundProfile?>(null)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        context = mockk(relaxed = true)
        contextEngine = mockk()
        ruleResolver = mockk()
        audioController = mockk()
        eventDao = mockk(relaxed = true)
        notificationManager = mockk(relaxed = true)
        hapticController = mockk(relaxed = true)
        preferencesManager = mockk(relaxed = true)

        every { contextEngine.observeContext() } returns kotlinx.coroutines.flow.filterNotNull(contextFlow)
        every { preferencesManager.lastResolvedProfileFlow } returns lastResolvedFlow
        
        orchestrator = AutomationOrchestrator(
            context,
            contextEngine,
            ruleResolver,
            audioController,
            eventDao,
            notificationManager,
            hapticController,
            preferencesManager
        )
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `orchestrator skips application if target profile equals physical profile`() = runTest(testDispatcher) {
        val snapshot = ContextSnapshot(emptyList(), null, SoundProfile.NORMAL, false, false)
        val resolution = EffectiveResolution(SoundProfile.NORMAL, ResolutionSource.DEFAULT_PREFERENCE, null, "Default")
        
        every { ruleResolver.resolve(snapshot) } returns resolution
        // Last resolved was null, meaning it's a new evaluation
        lastResolvedFlow.value = null
        // But physical is ALREADY NORMAL
        every { audioController.getCurrentProfile() } returns SoundProfile.NORMAL
        
        orchestrator.start(testScope)
        contextFlow.value = snapshot
        
        testDispatcher.scheduler.advanceUntilIdle()

        // It should NOT call applyProfile
        verify(exactly = 0) { audioController.applyProfile(any()) }
        // But it SHOULD save the last resolved target
        verify { preferencesManager.setLastResolvedProfile(SoundProfile.NORMAL) }
    }

    @Test
    fun `orchestrator applies new profile if differing from last resolved and hardware`() = runTest(testDispatcher) {
        val snapshot = ContextSnapshot(emptyList(), null, SoundProfile.VIBRATE, false, false)
        val resolution = EffectiveResolution(SoundProfile.VIBRATE, ResolutionSource.ACTIVE_RULE, "123", "Rule 123")
        
        every { ruleResolver.resolve(snapshot) } returns resolution
        lastResolvedFlow.value = SoundProfile.NORMAL
        every { audioController.getCurrentProfile() } returns SoundProfile.NORMAL
        every { audioController.applyProfile(SoundProfile.VIBRATE) } returns true

        orchestrator.start(testScope)
        contextFlow.value = snapshot
        
        testDispatcher.scheduler.advanceUntilIdle()

        verify(exactly = 1) { audioController.applyProfile(SoundProfile.VIBRATE) }
        verify(exactly = 1) { hapticController.performAutomationTransitionEffect() }
        verify(exactly = 1) { notificationManager.showAutomationStatus(any(), any()) }
        coVerify(exactly = 1) { eventDao.insertEvent(any()) }
        verify { preferencesManager.setLastResolvedProfile(SoundProfile.VIBRATE) }
    }

    @Test
    fun `orchestrator respects manual user override by not fighting hardware if target profile hasn't changed conceptually`() = runTest(testDispatcher) {
        val snapshot = ContextSnapshot(emptyList(), null, SoundProfile.VIBRATE, false, false)
        val resolution = EffectiveResolution(SoundProfile.VIBRATE, ResolutionSource.ACTIVE_RULE, "123", "Rule 123")
        
        every { ruleResolver.resolve(snapshot) } returns resolution
        // The orchestrator has ALREADY resolved Vibrate previously!
        lastResolvedFlow.value = SoundProfile.VIBRATE
        
        // But the user manually changed their phone to NORMAL using hardware keys
        every { audioController.getCurrentProfile() } returns SoundProfile.NORMAL

        orchestrator.start(testScope)
        contextFlow.value = snapshot
        
        testDispatcher.scheduler.advanceUntilIdle()

        // It should NOT re-apply Vibrate. It must respect the user's manual hardware action.
        verify(exactly = 0) { audioController.applyProfile(any()) }
    }
}
