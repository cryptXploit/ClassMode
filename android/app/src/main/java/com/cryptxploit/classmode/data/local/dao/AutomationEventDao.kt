package com.cryptxploit.classmode.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cryptxploit.classmode.data.local.entity.AutomationEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AutomationEventDao {
    @Query("SELECT * FROM automation_events ORDER BY timestamp DESC LIMIT 50")
    fun getRecentEvents(): Flow<List<AutomationEventEntity>>

    @Insert
    suspend fun insertEvent(event: AutomationEventEntity)
    
    @Query("DELETE FROM automation_events")
    suspend fun clearHistory()
}

