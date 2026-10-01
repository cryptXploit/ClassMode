package com.cryptxploit.classmode.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timeMins: Int,
    val daysOfWeek: Int,
    val isEnabled: Boolean = true,
    val isVibrationEnabled: Boolean = true,
    val snoozeMins: Int = 5,
    val label: String = ""
)

