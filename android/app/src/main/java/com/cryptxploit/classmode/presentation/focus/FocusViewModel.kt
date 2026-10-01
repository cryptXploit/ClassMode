package com.cryptxploit.classmode.presentation.focus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptxploit.classmode.data.local.dao.ScheduleDao
import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import com.cryptxploit.classmode.data.system.SystemAlarmScheduler
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class FocusViewModel(
    private val scheduleDao: ScheduleDao,
    private val alarmScheduler: SystemAlarmScheduler
) : ViewModel() {

    val activeFocusSession: StateFlow<ScheduleEntity?> = scheduleDao.getActiveSchedules()
        .map { schedules ->
            schedules.firstOrNull { it.type == SessionType.FOCUS }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun startFocusSession(durationMinutes: Int, profile: SoundProfile = SoundProfile.SILENT) {
        viewModelScope.launch {
            // End existing focus session if any
            val existing = scheduleDao.getSchedulesSync().firstOrNull { it.type == SessionType.FOCUS }
            if (existing != null) {
                alarmScheduler.cancelSchedule(existing.id)
                scheduleDao.deleteSchedule(existing)
            }

            val now = Calendar.getInstance()
            val currentDay = now.get(Calendar.DAY_OF_WEEK)
            val startMins = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
            val endMins = startMins + durationMinutes

            val daysOfWeekBitmask = 1 shl (currentDay - 1)

            val focusSession = ScheduleEntity(
                type = SessionType.FOCUS,
                title = "Focus Session",
                startTimeMins = startMins,
                endTimeMins = endMins,
                daysOfWeek = daysOfWeekBitmask,
                soundProfile = profile,
                isEnabled = true
            )

            val id = scheduleDao.insertSchedule(focusSession)
            
            // Re-fetch to get the entity with ID, then schedule it
            val inserted = scheduleDao.getScheduleById(id)
            if (inserted != null) {
                alarmScheduler.scheduleClass(inserted)
                
                // Immediately trigger the start alarm manually if we are within the time
                
                // We broadcast this context-less by just passing it?
                // Wait, we don't have Context here. SystemAlarmScheduler sets it for later. 
                // But the current time IS the start time. 
                // Since `alarmScheduler.scheduleClass` calls `setExactAlarm(nextStart)`...
                // Wait, if `nextStart` is in the past or exactly now, AlarmManager will fire it immediately.
                // So it will trigger `ACTION_CLASS_START` right away! 
            }
        }
    }

    fun stopFocusSession() {
        viewModelScope.launch {
            val existing = scheduleDao.getSchedulesSync().firstOrNull { it.type == SessionType.FOCUS }
            if (existing != null) {
                alarmScheduler.cancelSchedule(existing.id)
                scheduleDao.deleteSchedule(existing)
                
                // Also trigger an end action just in case we need to clean up trigger state
                // But actually, TriggerStateDao doesn't know about deleted rules in ContextEngine.
                // If a rule is deleted, ContextEngine activeSessions won't include it. 
                // So it will immediately resolve correctly!
            }
        }
    }
}

