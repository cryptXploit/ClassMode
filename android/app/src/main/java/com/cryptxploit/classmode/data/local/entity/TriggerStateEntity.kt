package com.cryptxploit.classmode.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trigger_states")
data class TriggerStateEntity(
    @PrimaryKey
    val ruleId: Long,
    val isTimeActive: Boolean = false,
    val isLocationActive: Boolean = false,
    val lastUpdated: Long = 0L
)

