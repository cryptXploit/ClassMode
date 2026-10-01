package com.cryptxploit.classmode.data.repository

import com.cryptxploit.classmode.data.local.dao.AlarmDao
import com.cryptxploit.classmode.data.local.entity.AlarmEntity
import com.cryptxploit.classmode.domain.model.AlarmDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlarmRepository(private val alarmDao: AlarmDao) {

    val allAlarms: Flow<List<AlarmDomainModel>> = alarmDao.getAllAlarms().map { entities ->
        entities.map { it.toDomainModel() }
    }

    suspend fun getAlarmById(id: Long): AlarmDomainModel? {
        return alarmDao.getAlarmById(id)?.toDomainModel()
    }

    suspend fun insertAlarm(alarm: AlarmDomainModel): Long {
        return alarmDao.insertAlarm(alarm.toEntity())
    }

    suspend fun updateAlarm(alarm: AlarmDomainModel) {
        alarmDao.updateAlarm(alarm.toEntity())
    }

    suspend fun deleteAlarm(alarm: AlarmDomainModel) {
        alarmDao.deleteAlarm(alarm.toEntity())
    }

    private fun AlarmEntity.toDomainModel(): AlarmDomainModel {
        return AlarmDomainModel(
            id = id,
            timeMins = timeMins,
            daysOfWeek = daysOfWeek,
            isEnabled = isEnabled,
            isVibrationEnabled = isVibrationEnabled,
            snoozeMins = snoozeMins,
            label = label
        )
    }

    private fun AlarmDomainModel.toEntity(): AlarmEntity {
        return AlarmEntity(
            id = id,
            timeMins = timeMins,
            daysOfWeek = daysOfWeek,
            isEnabled = isEnabled,
            isVibrationEnabled = isVibrationEnabled,
            snoozeMins = snoozeMins,
            label = label
        )
    }
}

