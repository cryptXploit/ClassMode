package com.cryptxploit.classmode.presentation.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cryptxploit.classmode.data.local.dao.GeofenceDao
import com.cryptxploit.classmode.data.local.dao.ScheduleDao
import com.cryptxploit.classmode.data.local.entity.GeofenceEntity
import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import com.cryptxploit.classmode.data.preferences.PreferencesManager
import com.cryptxploit.classmode.data.system.GeofenceManager
import com.cryptxploit.classmode.domain.model.AutomationRuleCondition
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LocationViewModel(
    private val geofenceDao: GeofenceDao,
    private val geofenceManager: GeofenceManager,
    private val scheduleDao: ScheduleDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val geofences = geofenceDao.getAllGeofences().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addGeofence(latitude: Double, longitude: Double, radius: Float) {
        viewModelScope.launch {
            val ruleId = System.currentTimeMillis()
            
            // Get current default profile from settings
            val defaultProfile = preferencesManager.defaultProfileFlow.first()
            
            // Create a corresponding ScheduleEntity so the automation engine recognizes it
            val schedule = ScheduleEntity(
                id = ruleId,
                type = SessionType.CLASS,
                title = "Location Auto-Rule",
                startTimeMins = 0,
                endTimeMins = 0,
                daysOfWeek = 127, // Everyday (all 7 bits set)
                soundProfile = defaultProfile,
                condition = AutomationRuleCondition.LOCATION_ONLY,
                isEnabled = true
            )
            scheduleDao.insertSchedule(schedule)

            val entity = GeofenceEntity(
                ruleId = ruleId,
                latitude = latitude,
                longitude = longitude,
                radiusMeters = radius
            )
            
            // Persist to Room
            geofenceDao.insertGeofence(entity)
            
            // Register with GeofencingClient
            geofenceManager.registerGeofence(entity)
        }
    }

    fun deleteGeofence(geofence: GeofenceEntity) {
        viewModelScope.launch {
            geofenceDao.deleteGeofence(geofence)
            val schedule = scheduleDao.getScheduleById(geofence.ruleId)
            if (schedule != null) {
                scheduleDao.deleteSchedule(schedule)
            }
            geofenceManager.removeGeofence(geofence.ruleId)
        }
    }
}

