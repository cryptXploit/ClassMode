package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import com.cryptxploit.classmode.domain.model.AutomationRuleCondition
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class ScheduleCalculatorTest {

    private fun createSchedule(
        startTimeMins: Int,
        endTimeMins: Int,
        daysOfWeek: Int,
        isEnabled: Boolean = true
    ): ScheduleEntity {
        return ScheduleEntity(
            id = 1L,
            name = "Test Schedule",
            isEnabled = isEnabled,
            startTimeMins = startTimeMins,
            endTimeMins = endTimeMins,
            daysOfWeek = daysOfWeek,
            soundProfile = SoundProfile.SILENT,
            type = SessionType.CLASS,
            condition = AutomationRuleCondition.TIME_ONLY
        )
    }

    private fun getTimeMillis(dayOfWeek: Int, hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance()
        // Anchor to a specific week to avoid rolling issues in tests
        cal.set(Calendar.YEAR, 2026)
        cal.set(Calendar.MONTH, Calendar.OCTOBER)
        cal.set(Calendar.WEEK_OF_MONTH, 2)
        cal.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    @Test
    fun `isCurrentlyActive same day exact boundaries`() {
        // Monday (bit 2) from 16:00 to 17:00
        val schedule = createSchedule(16 * 60, 17 * 60, 2)

        // 15:59 - Inactive
        assertFalse(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.MONDAY, 15, 59)))
        
        // 16:00 - Active (Inclusive start)
        assertTrue(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.MONDAY, 16, 0)))
        
        // 16:30 - Active
        assertTrue(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.MONDAY, 16, 30)))
        
        // 16:59 - Active
        assertTrue(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.MONDAY, 16, 59)))
        
        // 17:00 - Inactive (Exclusive end)
        assertFalse(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.MONDAY, 17, 0)))
    }

    @Test
    fun `isCurrentlyActive wrong day`() {
        // Monday (bit 2) from 16:00 to 17:00
        val schedule = createSchedule(16 * 60, 17 * 60, 2)

        // Tuesday at 16:30 - Inactive
        assertFalse(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.TUESDAY, 16, 30)))
    }

    @Test
    fun `isCurrentlyActive disabled schedule`() {
        val schedule = createSchedule(16 * 60, 17 * 60, 2, isEnabled = false)
        assertFalse(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.MONDAY, 16, 30)))
    }

    @Test
    fun `isCurrentlyActive overnight schedule`() {
        // Monday (bit 2) from 22:00 to 02:00
        val schedule = createSchedule(22 * 60, 2 * 60, 2)

        // Monday 21:59 - Inactive
        assertFalse(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.MONDAY, 21, 59)))
        
        // Monday 22:00 - Active
        assertTrue(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.MONDAY, 22, 0)))
        
        // Tuesday 01:59 - Active (Because it started on Monday night)
        assertTrue(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.TUESDAY, 1, 59)))
        
        // Tuesday 02:00 - Inactive
        assertFalse(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.TUESDAY, 2, 0)))
    }

    @Test
    fun `isCurrentlyActive overnight schedule wrong previous day`() {
        // Monday (bit 2) from 22:00 to 02:00
        val schedule = createSchedule(22 * 60, 2 * 60, 2)

        // Wednesday 01:59 - Inactive (Tuesday night didn't start the schedule)
        assertFalse(ScheduleCalculator.isCurrentlyActive(schedule, getTimeMillis(Calendar.WEDNESDAY, 1, 59)))
    }

    @Test
    fun `getNextTriggerTime future on same day`() {
        val schedule = createSchedule(16 * 60, 17 * 60, 2) // Monday
        val now = getTimeMillis(Calendar.MONDAY, 10, 0)
        
        val nextStart = ScheduleCalculator.getNextTriggerTime(schedule, true, now)
        val expectedStart = getTimeMillis(Calendar.MONDAY, 16, 0)
        
        assertEquals(expectedStart, nextStart)
    }

    @Test
    fun `getNextTriggerTime rolls over to next week`() {
        val schedule = createSchedule(16 * 60, 17 * 60, 2) // Monday
        val now = getTimeMillis(Calendar.MONDAY, 18, 0) // Past the start time
        
        val nextStart = ScheduleCalculator.getNextTriggerTime(schedule, true, now)
        
        val cal = Calendar.getInstance().apply { timeInMillis = now }
        cal.add(Calendar.DAY_OF_YEAR, 7)
        cal.set(Calendar.HOUR_OF_DAY, 16)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        
        assertEquals(cal.timeInMillis, nextStart)
    }

    @Test
    fun `getNextTriggerTime disabled returns -1`() {
        val schedule = createSchedule(16 * 60, 17 * 60, 2, isEnabled = false)
        val now = getTimeMillis(Calendar.MONDAY, 10, 0)
        val nextStart = ScheduleCalculator.getNextTriggerTime(schedule, true, now)
        assertEquals(-1L, nextStart)
    }
}
