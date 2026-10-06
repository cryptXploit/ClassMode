package com.cryptxploit.classmode.domain.automation

import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import java.util.Calendar

object ScheduleCalculator {

    /**
     * Determines the next trigger time in milliseconds for a schedule.
     * @param schedule The schedule configuration.
     * @param isStart True to calculate the start time, false to calculate the end time.
     * @param currentTimeMillis The current time in milliseconds. Defaults to System.currentTimeMillis().
     * @return The exact Unix timestamp of the next trigger event.
     */
    fun getNextTriggerTime(schedule: ScheduleEntity, isStart: Boolean, currentTimeMillis: Long = System.currentTimeMillis()): Long {
        if (!schedule.isEnabled) return -1L
        
        val now = Calendar.getInstance().apply { timeInMillis = currentTimeMillis }
        var bestTime = Long.MAX_VALUE

        val targetMinute = if (isStart) schedule.startTimeMins else schedule.endTimeMins
        val targetHour = targetMinute / 60
        val targetMin = targetMinute % 60

        for (dayIdx in 1..7) {
            val bit = 1 shl (dayIdx - 1)
            if ((schedule.daysOfWeek and bit) != 0) {
                val calendar = Calendar.getInstance().apply { timeInMillis = currentTimeMillis }
                calendar.set(Calendar.HOUR_OF_DAY, targetHour)
                calendar.set(Calendar.MINUTE, targetMin)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
                var daysToAdd = dayIdx - currentDay
                
                // If the target day is in the past, or it is today but the target time has already passed
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

    /**
     * Determines if a schedule is actively running at the given time.
     * @param schedule The schedule configuration.
     * @param currentTimeMillis The time to check against.
     * @return True if the schedule is conceptually ACTIVE at the given time.
     */
    fun isCurrentlyActive(schedule: ScheduleEntity, currentTimeMillis: Long = System.currentTimeMillis()): Boolean {
        if (!schedule.isEnabled) return false
        
        val now = Calendar.getInstance().apply { timeInMillis = currentTimeMillis }
        val currentMins = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
        val currentDay = now.get(Calendar.DAY_OF_WEEK)
        val currentDayBit = 1 shl (currentDay - 1)
        
        val startMins = schedule.startTimeMins
        val endMins = schedule.endTimeMins
        
        if (startMins < endMins) {
            // Same day
            val isDayMatch = (schedule.daysOfWeek and currentDayBit) != 0
            // start inclusive, end exclusive
            return isDayMatch && currentMins >= startMins && currentMins < endMins
        } else if (startMins > endMins) {
            // Overnight
            val previousDay = if (currentDay == 1) 7 else currentDay - 1
            val previousDayBit = 1 shl (previousDay - 1)
            
            // Matches tonight (started earlier today, going into tomorrow)
            val matchesTonight = (schedule.daysOfWeek and currentDayBit) != 0 && currentMins >= startMins
            
            // Matches last night (started yesterday, ending today)
            val matchesLastNight = (schedule.daysOfWeek and previousDayBit) != 0 && currentMins < endMins
            
            return matchesTonight || matchesLastNight
        } else {
            // Start == End (0-minute schedule). Cannot logically be active.
            return false
        }
    }
}
