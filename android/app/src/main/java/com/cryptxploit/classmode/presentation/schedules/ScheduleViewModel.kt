package com.cryptxploit.classmode.presentation.schedules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptxploit.classmode.data.local.dao.ScheduleDao
import com.cryptxploit.classmode.data.local.dao.GeofenceDao
import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import com.cryptxploit.classmode.data.local.entity.GeofenceEntity
import com.cryptxploit.classmode.data.system.SystemAlarmScheduler
import com.cryptxploit.classmode.data.system.GeofenceManager
import com.cryptxploit.classmode.domain.model.AutomationRuleCondition
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val scheduleDao: ScheduleDao,
    private val alarmScheduler: SystemAlarmScheduler,
    private val geofenceDao: GeofenceDao,
    private val geofenceManager: GeofenceManager
) : ViewModel() {

    val schedules = scheduleDao.getAllSchedules().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun toggleSchedule(schedule: ScheduleEntity, isActive: Boolean) {
        viewModelScope.launch {
            if (!isActive) {
                alarmScheduler.cancelSchedule(schedule.id)
                geofenceManager.removeGeofence(schedule.id)
            }
            val updated = schedule.copy(isEnabled = isActive)
            scheduleDao.updateSchedule(updated)
            if (isActive) {
                alarmScheduler.scheduleClass(updated)
                val geo = geofenceDao.getGeofenceByRuleId(updated.id)
                if (geo != null) geofenceManager.registerGeofence(geo)
            }
        }
    }

    fun deleteSchedule(schedule: ScheduleEntity) {
        viewModelScope.launch {
            alarmScheduler.cancelSchedule(schedule.id)
            geofenceManager.removeGeofence(schedule.id)
            geofenceDao.deleteGeofenceByRuleId(schedule.id)
            scheduleDao.deleteSchedule(schedule)
        }
    }

    fun saveSchedule(schedule: ScheduleEntity, geofence: GeofenceEntity?) {
        viewModelScope.launch {
            val isNew = schedule.id == 0L
            
            if (!isNew) {
                alarmScheduler.cancelSchedule(schedule.id)
                geofenceManager.removeGeofence(schedule.id)
            }
            
            val id = if (isNew) scheduleDao.insertSchedule(schedule) else { scheduleDao.updateSchedule(schedule); schedule.id }
            val finalSchedule = schedule.copy(id = id)
            
            if (finalSchedule.isEnabled) {
                alarmScheduler.scheduleClass(finalSchedule)
            }

            if (geofence != null) {
                val geoToSave = geofence.copy(ruleId = id)
                geofenceDao.deleteGeofenceByRuleId(id)
                geofenceDao.insertGeofence(geoToSave)
                if (finalSchedule.isEnabled) geofenceManager.registerGeofence(geoToSave)
            } else {
                geofenceDao.deleteGeofenceByRuleId(id)
                if (isNew) geofenceManager.removeGeofence(id) // ensure cleanup
            }
        }
    }
}

