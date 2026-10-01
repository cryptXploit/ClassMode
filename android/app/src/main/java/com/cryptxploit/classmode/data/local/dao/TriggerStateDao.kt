package com.cryptxploit.classmode.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cryptxploit.classmode.data.local.entity.TriggerStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TriggerStateDao {
    @Query("SELECT * FROM trigger_states")
    fun observeAllStates(): Flow<List<TriggerStateEntity>>

    @Query("SELECT * FROM trigger_states WHERE ruleId = :ruleId")
    suspend fun getState(ruleId: Long): TriggerStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(state: TriggerStateEntity)
    
    @Query("UPDATE trigger_states SET isTimeActive = :isActive, lastUpdated = :timestamp WHERE ruleId = :ruleId")
    suspend fun updateTimeState(ruleId: Long, isActive: Boolean, timestamp: Long)

    @Query("UPDATE trigger_states SET isLocationActive = :isActive, lastUpdated = :timestamp WHERE ruleId = :ruleId")
    suspend fun updateLocationState(ruleId: Long, isActive: Boolean, timestamp: Long)
    
    @Query("DELETE FROM trigger_states WHERE ruleId = :ruleId")
    suspend fun deleteState(ruleId: Long)
}

