package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.data.preferences.RestoreStateRepository
import com.cryptxploit.classmode.data.system.SystemAudioController
import com.cryptxploit.classmode.domain.model.SoundProfile
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.cryptxploit.classmode.domain.model.ActiveAutomationTarget
import com.cryptxploit.classmode.domain.model.CapturedState

class RestoreStateManagerTest {

    private lateinit var audioController: SystemAudioController
    private lateinit var stateRepository: RestoreStateRepository
    private lateinit var healthMonitor: AutomationHealthMonitor
    private lateinit var manager: RestoreStateManager

    private val originalStateFlow = MutableStateFlow<CapturedState?>(null)
    private val activeTargetFlow = MutableStateFlow<ActiveAutomationTarget?>(null)

    @Before
    fun setup() {
        audioController = mockk(relaxed = true)
        stateRepository = mockk(relaxed = true)
        healthMonitor = mockk(relaxed = true)

        every { stateRepository.originalStateFlow } returns originalStateFlow
        every { stateRepository.activeTargetFlow } returns activeTargetFlow

        manager = RestoreStateManager(audioController, stateRepository, healthMonitor)
    }

    @Test
    fun `captureAndApply saves original state if no existing target`() = runTest {
        // Given
        val currentProfile = SoundProfile.NORMAL
        every { audioController.getCurrentProfile() } returns currentProfile
        activeTargetFlow.value = null
        coEvery { audioController.applyProfile(SoundProfile.SILENT) } returns true

        // When
        manager.captureAndApply("session1", SoundProfile.SILENT)

        // Then
        verify { stateRepository.saveOriginalState(SoundProfile.NORMAL) }
        verify { stateRepository.saveActiveTarget("session1", SoundProfile.SILENT) }
    }

    @Test
    fun `captureAndApply does not overwrite original state if target exists`() = runTest {
        // Given
        val currentProfile = SoundProfile.SILENT
        every { audioController.getCurrentProfile() } returns currentProfile
        activeTargetFlow.value = ActiveAutomationTarget("session1", SoundProfile.SILENT, 0L)
        coEvery { audioController.applyProfile(SoundProfile.VIBRATE) } returns true

        // When
        manager.captureAndApply("session2", SoundProfile.VIBRATE)

        // Then
        verify(exactly = 0) { stateRepository.saveOriginalState(any(), any(), any()) }
        verify { stateRepository.saveActiveTarget("session2", SoundProfile.VIBRATE) }
    }

    @Test
    fun `evaluateAndRestore restores original state if no manual override`() = runTest {
        // Given
        originalStateFlow.value = CapturedState(SoundProfile.NORMAL, 0L, 2, 0)
        activeTargetFlow.value = ActiveAutomationTarget("session1", SoundProfile.SILENT, 0L)
        
        every { audioController.getCurrentProfile() } returns SoundProfile.SILENT // No override
        coEvery { audioController.applyProfile(SoundProfile.NORMAL) } returns true

        // When
        manager.evaluateAndRestore("session1")

        // Then
        coVerify { audioController.applyProfile(SoundProfile.NORMAL) }
        verify { stateRepository.clearState() }
    }

    @Test
    fun `evaluateAndRestore aborts restore if manual override detected`() = runTest {
        // Given
        originalStateFlow.value = CapturedState(SoundProfile.NORMAL, 0L, 2, 0)
        activeTargetFlow.value = ActiveAutomationTarget("session1", SoundProfile.SILENT, 0L)
        
        every { audioController.getCurrentProfile() } returns SoundProfile.VIBRATE // User changed to Vibrate!

        // When
        manager.evaluateAndRestore("session1")

        // Then
        coVerify(exactly = 0) { audioController.applyProfile(any()) }
        verify { stateRepository.clearState() } // clears the state but doesn't restore
    }

    @Test
    fun `evaluateAndRestore aborts if ending session is not the active target`() = runTest {
        // Given
        originalStateFlow.value = CapturedState(SoundProfile.NORMAL, 0L, 2, 0)
        activeTargetFlow.value = ActiveAutomationTarget("session2", SoundProfile.SILENT, 0L)
        
        // When session1 ends but session2 is the active one
        manager.evaluateAndRestore("session1")

        // Then
        coVerify(exactly = 0) { audioController.applyProfile(any()) }
        verify(exactly = 0) { stateRepository.clearState() }
    }
}

