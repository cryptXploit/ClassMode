package com.cryptxploit.classmode.data.local.dao

import androidx.room.*
import com.cryptxploit.classmode.data.local.entity.GeofenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GeofenceDao {
    @Query("SELECT * FROM geofences")
    fun getAllGeofences(): Flow<List<GeofenceEntity>>

    @Query("SELECT * FROM geofences WHERE ruleId = :ruleId")
    suspend fun getGeofenceByRuleId(ruleId: Long): GeofenceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeofence(geofence: GeofenceEntity): Long

    @Delete
    suspend fun deleteGeofence(geofence: GeofenceEntity)

    @Query("DELETE FROM geofences WHERE ruleId = :ruleId")
    suspend fun deleteGeofenceByRuleId(ruleId: Long)
}

