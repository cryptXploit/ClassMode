package com.cryptxploit.classmode.presentation.alarms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptxploit.classmode.data.repository.AlarmRepository
import com.cryptxploit.classmode.data.system.SystemAlarmScheduler
import com.cryptxploit.classmode.domain.model.AlarmDomainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmViewModel(
    private val repository: AlarmRepository,
    private val scheduler: SystemAlarmScheduler
) : ViewModel() {

    private val _alarms = MutableStateFlow<List<AlarmDomainModel>>(emptyList())
    val alarms: StateFlow<List<AlarmDomainModel>> = _alarms.asStateFlow()

    init {
        viewModelScope.launch {
            repository.allAlarms.collect { list ->
                _alarms.value = list
            }
        }
    }

    fun addAlarm(timeMins: Int, daysOfWeek: Int, label: String, isVibrationEnabled: Boolean, snoozeMins: Int) {
        viewModelScope.launch {
            val newAlarm = AlarmDomainModel(
                id = 0,
                timeMins = timeMins,
                daysOfWeek = daysOfWeek,
                isEnabled = true,
                isVibrationEnabled = isVibrationEnabled,
                snoozeMins = snoozeMins,
                label = label
            )
            val id = repository.insertAlarm(newAlarm)
            scheduleAlarm(newAlarm.copy(id = id))
        }
    }

    fun updateAlarm(alarm: AlarmDomainModel) {
        viewModelScope.launch {
            repository.updateAlarm(alarm)
            if (alarm.isEnabled) {
                scheduleAlarm(alarm)
            } else {
                scheduler.cancelUserAlarm(alarm.id)
            }
        }
    }

    fun deleteAlarm(alarm: AlarmDomainModel) {
        viewModelScope.launch {
            repository.deleteAlarm(alarm)
            scheduler.cancelUserAlarm(alarm.id)
        }
    }

    fun toggleAlarm(alarm: AlarmDomainModel, isEnabled: Boolean) {
        updateAlarm(alarm.copy(isEnabled = isEnabled))
    }

    private fun scheduleAlarm(alarm: AlarmDomainModel) {
        val triggerTime = calculateNextTrigger(alarm.timeMins, alarm.daysOfWeek)
        if (triggerTime != -1L) {
            scheduler.scheduleUserAlarm(alarm, triggerTime)
        }
    }

    private fun calculateNextTrigger(timeMins: Int, daysOfWeek: Int): Long {
        val cal = Calendar.getInstance()
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)
        val currentMinute = cal.get(Calendar.MINUTE)
        val currentTotalMins = currentHour * 60 + currentMinute
        
        var targetDay = cal.get(Calendar.DAY_OF_WEEK)
        // Adjust for Calendar mapping (Sunday=1, Monday=2) to bitmask (Monday=1...Sunday=64)
        // If daysOfWeek is 0, just ring once (today or tomorrow)
        if (daysOfWeek == 0) {
            cal.set(Calendar.HOUR_OF_DAY, timeMins / 60)
            cal.set(Calendar.MINUTE, timeMins % 60)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MILLISECOND, 0)
            if (timeMins <= currentTotalMins) {
                cal.add(Calendar.DAY_OF_YEAR, 1)
            }
            return cal.timeInMillis
        }
        
        // Find next day in bitmask
        for (i in 0..7) {
            val dayToTest = (targetDay - 2 + i + 7) % 7 // Monday = 0
            val bit = 1 shl dayToTest
            if ((daysOfWeek and bit) != 0) {
                if (i == 0 && timeMins <= currentTotalMins) {
                    continue // Try next matched day
                }
                cal.add(Calendar.DAY_OF_YEAR, i)
                cal.set(Calendar.HOUR_OF_DAY, timeMins / 60)
                cal.set(Calendar.MINUTE, timeMins % 60)
                cal.set(Calendar.SECOND, 0)
                cal.set(Calendar.MILLISECOND, 0)
                return cal.timeInMillis
            }
        }
        return -1L
    }
}

