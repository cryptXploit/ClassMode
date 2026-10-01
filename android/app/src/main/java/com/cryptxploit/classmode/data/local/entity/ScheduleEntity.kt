package com.cryptxploit.classmode.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import com.cryptxploit.classmode.domain.model.AutomationRuleCondition

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: SessionType,
    val title: String = "",
    val startTimeMins: Int,
    val endTimeMins: Int,
    val daysOfWeek: Int,
    val soundProfile: SoundProfile = SoundProfile.VIBRATE,
    val condition: AutomationRuleCondition = AutomationRuleCondition.TIME_ONLY,
    val isEnabled: Boolean = true
)

@Entity(tableName = "geofences")
data class GeofenceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ruleId: Long,
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Float
)

