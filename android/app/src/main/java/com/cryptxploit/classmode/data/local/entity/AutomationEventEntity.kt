package com.cryptxploit.classmode.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cryptxploit.classmode.domain.model.CapabilityResult

@Entity(tableName = "automation_events")
data class AutomationEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ruleId: Long,
    val timestamp: Long,
    val result: CapabilityResult,
    val details: String
)

