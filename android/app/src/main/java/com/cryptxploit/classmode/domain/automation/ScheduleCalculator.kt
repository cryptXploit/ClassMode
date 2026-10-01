package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import java.util.Calendar

object ScheduleCalculator {

    // daysOfWeek is expected to be a bitmask where bit 0 is Sunday, bit 1 is Monday, etc.
    // e.g. 1 = Sunday, 2 = Monday, 4 = Tuesday, 64 = Saturday.
    // But let's check standard Android Calendar: SUNDAY = 1, MONDAY = 2... SATURDAY = 7
    // So if the bitmask is 1 << (Calendar.DAY_OF_WEEK - 1), 
    // Sunday (1) -> 1 << 0 = 1
    // Monday (2) -> 1 << 1 = 2
    
    fun getNextTriggerTime(schedule: ScheduleEntity, isStart: Boolean): Long {
        val now = Calendar.getInstance()
        var bestTime = Long.MAX_VALUE

        val targetMinute = if (isStart) schedule.startTimeMins else schedule.endTimeMins
        val targetHour = targetMinute / 60
        val targetMin = targetMinute % 60

        for (dayIdx in 1..7) {
            val bit = 1 shl (dayIdx - 1)
            if ((schedule.daysOfWeek and bit) != 0) {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, targetHour)
                calendar.set(Calendar.MINUTE, targetMin)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
                var daysToAdd = dayIdx - currentDay
                
                if (daysToAdd < 0 || (daysToAdd == 0 && calendar.timeInMillis <= now.timeInMillis)) {
                    daysToAdd += 7
                }
                
                calendar.add(Calendar.DAY_OF_YEAR, daysToAdd)
                
                if (calendar.timeInMillis < bestTime) {
                    bestTime = calendar.timeInMillis
                }
            }
        }
        
        return if (bestTime == Long.MAX_VALUE) -1L else bestTime
    }

    fun isCurrentlyActive(schedule: ScheduleEntity): Boolean {
        val now = Calendar.getInstance()
        val currentMins = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
        val currentDay = now.get(Calendar.DAY_OF_WEEK)
        val currentDayBit = 1 shl (currentDay - 1)
        
        val startMins = schedule.startTimeMins
        val endMins = schedule.endTimeMins
        
        if (startMins < endMins) {
            // Same day
            val isDayMatch = (schedule.daysOfWeek and currentDayBit) != 0
            return isDayMatch && currentMins >= startMins && currentMins < endMins
        } else {
            // Overnight
            val previousDay = if (currentDay == 1) 7 else currentDay - 1
            val previousDayBit = 1 shl (previousDay - 1)
            
            val matchesTonight = (schedule.daysOfWeek and currentDayBit) != 0 && currentMins >= startMins
            val matchesLastNight = (schedule.daysOfWeek and previousDayBit) != 0 && currentMins < endMins
            
            return matchesTonight || matchesLastNight
        }
    }
}

