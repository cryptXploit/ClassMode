package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.data.local.dao.ScheduleDao
import com.cryptxploit.classmode.data.local.dao.TriggerStateDao
import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import com.cryptxploit.classmode.data.local.entity.TriggerStateEntity
import com.cryptxploit.classmode.data.preferences.PreferencesManager
import com.cryptxploit.classmode.data.system.GeofenceManager
import com.cryptxploit.classmode.data.system.SystemAudioController
import com.cryptxploit.classmode.domain.model.AutomationRuleCondition
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ContextEngineTest {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var scheduleDao: ScheduleDao
    private lateinit var triggerStateDao: TriggerStateDao
    private lateinit var systemAudioController: SystemAudioController
    private lateinit var geofenceManager: GeofenceManager
    private lateinit var engine: ContextEngine

    private val defaultProfileFlow = MutableStateFlow(SoundProfile.NORMAL)
    private val overrideProfileFlow = MutableStateFlow<SoundProfile?>(null)
    private val schedulesFlow = MutableStateFlow<List<ScheduleEntity>>(emptyList())
    private val triggerStatesFlow = MutableStateFlow<List<TriggerStateEntity>>(emptyList())

    @Before
    fun setup() {
        preferencesManager = mockk()
        scheduleDao = mockk()
        triggerStateDao = mockk()
        systemAudioController = mockk()
        geofenceManager = mockk()

        every { preferencesManager.defaultProfileFlow } returns defaultProfileFlow
        every { preferencesManager.userOverrideFlow } returns overrideProfileFlow
        every { scheduleDao.getActiveSchedules() } returns schedulesFlow
        every { triggerStateDao.observeAllStates() } returns triggerStatesFlow
        
        every { systemAudioController.getCurrentProfile() } returns SoundProfile.NORMAL
        every { geofenceManager.hasLocationPermission() } returns true

        engine = ContextEngine(
            preferencesManager,
            scheduleDao,
            triggerStateDao,
            systemAudioController,
            geofenceManager
        )

        mockkObject(ScheduleCalculator)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    private fun createSchedule(
        id: Long,
        condition: AutomationRuleCondition
    ): ScheduleEntity {
        return ScheduleEntity(
            id = id,
            title = "Test",
            startTimeMins = 0,
            endTimeMins = 0,
            daysOfWeek = 127,
            soundProfile = SoundProfile.SILENT,
            type = SessionType.CLASS,
            condition = condition
        )
    }

    @Test
    fun `time only schedule ignores missing trigger state`() = runTest {
        val schedule = createSchedule(1L, AutomationRuleCondition.TIME_ONLY)
        schedulesFlow.value = listOf(schedule)
        // triggerStatesFlow is empty!

        every { ScheduleCalculator.isCurrentlyActive(schedule, any()) } returns true

        val snapshot = engine.observeContext().first()

        assertEquals(1, snapshot.activeSessions.size)
        assertEquals("1", snapshot.activeSessions[0].id)
    }

    @Test
    fun `location only schedule requires trigger state`() = runTest {
        val schedule = createSchedule(1L, AutomationRuleCondition.LOCATION_ONLY)
        schedulesFlow.value = listOf(schedule)
        
        // No trigger state -> inactive
        triggerStatesFlow.value = emptyList()
        var snapshot = engine.observeContext().first()
        assertTrue(snapshot.activeSessions.isEmpty())

        // Add active location state
        triggerStatesFlow.value = listOf(TriggerStateEntity(1L, isLocationActive = true, lastUpdated = 0L))
        snapshot = engine.observeContext().first()
        assertEquals(1, snapshot.activeSessions.size)
    }

    @Test
    fun `time and location rule needs both`() = runTest {
        val schedule = createSchedule(1L, AutomationRuleCondition.TIME_AND_LOCATION)
        schedulesFlow.value = listOf(schedule)
        
        // 1. Time active, location not active -> inactive
        every { ScheduleCalculator.isCurrentlyActive(schedule, any()) } returns true
        triggerStatesFlow.value = listOf(TriggerStateEntity(1L, isLocationActive = false, lastUpdated = 0L))
        var snapshot = engine.observeContext().first()
        assertTrue(snapshot.activeSessions.isEmpty())

        // 2. Time inactive, location active -> inactive
        every { ScheduleCalculator.isCurrentlyActive(schedule, any()) } returns false
        triggerStatesFlow.value = listOf(TriggerStateEntity(1L, isLocationActive = true, lastUpdated = 0L))
        snapshot = engine.observeContext().first()
        assertTrue(snapshot.activeSessions.isEmpty())

        // 3. Both active -> active
        every { ScheduleCalculator.isCurrentlyActive(schedule, any()) } returns true
        triggerStatesFlow.value = listOf(TriggerStateEntity(1L, isLocationActive = true, lastUpdated = 0L))
        snapshot = engine.observeContext().first()
        assertEquals(1, snapshot.activeSessions.size)
    }

    @Test
    fun `respects missing location permission`() = runTest {
        val schedule = createSchedule(1L, AutomationRuleCondition.LOCATION_ONLY)
        schedulesFlow.value = listOf(schedule)
        triggerStatesFlow.value = listOf(TriggerStateEntity(1L, isLocationActive = true, lastUpdated = 0L))
        
        // If permission missing, location should be treated as false
        every { geofenceManager.hasLocationPermission() } returns false
        
        val snapshot = engine.observeContext().first()
        assertTrue(snapshot.activeSessions.isEmpty())
        assertTrue(snapshot.isLocationUnavailable)
    }
}
