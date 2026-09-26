package com.classmode.data.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.classmode.data.local.entity.ScheduleEntity
import com.classmode.data.receiver.AlarmReceiver
import com.classmode.domain.automation.AutomationHealthMonitor
import com.classmode.domain.automation.ScheduleCalculator
import com.classmode.data.system.SystemNotificationManager

class SystemAlarmScheduler(
    private val context: Context,
    private val healthMonitor: AutomationHealthMonitor
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    fun scheduleClass(schedule: ScheduleEntity) {
        val nextStart = ScheduleCalculator.getNextTriggerTime(schedule, isStart = true)
        val nextEnd = ScheduleCalculator.getNextTriggerTime(schedule, isStart = false)

        if (nextStart != -1L) {
            setExactAlarm(nextStart, schedule.id, isStart = true)
        }
        if (nextEnd != -1L) {
            setExactAlarm(nextEnd, schedule.id, isStart = false)
        }
    }

    private fun setExactAlarm(timeInMillis: Long, scheduleId: Long, isStart: Boolean) {
        if (timeInMillis - System.currentTimeMillis() > 15 * 60 * 1000 && isStart) {
            setReminderAlarm(timeInMillis - 15 * 60 * 1000, scheduleId)
        }
        if (!canScheduleExactAlarms()) {
            healthMonitor.logFailure("Cannot schedule exact alarm for schedule `$scheduleId. Permission denied.")
            SystemNotificationManager(context).showProblemNotification("Permission Required", "ClassMode needs permission to schedule exact alarms.")
            return
        }

        val action = if (isStart) AlarmReceiver.ACTION_CLASS_START else AlarmReceiver.ACTION_CLASS_END
        val requestCode = (scheduleId * 10 + (if (isStart) 1 else 0)).toInt()

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.action = action
            putExtra(AlarmReceiver.EXTRA_SESSION_ID, scheduleId.toString())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
            healthMonitor.logSuccess("Scheduled `$action for schedule `$scheduleId at `$timeInMillis")
        } catch (e: SecurityException) {
            healthMonitor.logFailure("SecurityException while scheduling exact alarm for `$scheduleId.")
        }
    }

    private fun setReminderAlarm(timeInMillis: Long, scheduleId: Long) {
        val requestCode = (scheduleId * 10 + 2).toInt()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.action = AlarmReceiver.ACTION_CLASS_REMINDER
            putExtra(AlarmReceiver.EXTRA_SESSION_ID, scheduleId.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        try {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        } catch (e: SecurityException) {
            // Ignore
        }
    }

    fun cancelSchedule(scheduleId: Long) {
        cancelAlarm(scheduleId, isStart = true)
        cancelAlarm(scheduleId, isStart = false)
        cancelReminder(scheduleId)
    }

    private fun cancelAlarm(scheduleId: Long, isStart: Boolean) {
        val action = if (isStart) AlarmReceiver.ACTION_CLASS_START else AlarmReceiver.ACTION_CLASS_END
        val requestCode = (scheduleId * 10 + (if (isStart) 1 else 0)).toInt()

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            healthMonitor.logSuccess("Cancelled `$action for schedule `$scheduleId")
        }
    }

    private fun cancelReminder(scheduleId: Long) {
        val requestCode = (scheduleId * 10 + 2).toInt()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.action = AlarmReceiver.ACTION_CLASS_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun scheduleUserAlarm(alarm: com.classmode.domain.model.AlarmDomainModel, triggerTimeInMillis: Long) {
        if (!canScheduleExactAlarms()) {
            SystemNotificationManager(context).showProblemNotification("Permission Required", "ClassMode needs permission to schedule exact alarms.")
            return
        }

        val requestCode = (alarm.id * 100 + 5).toInt()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.action = AlarmReceiver.ACTION_ALARM_RING
            putExtra("extra_alarm_id", alarm.id.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val showIntent = Intent(context, Class.forName("com.classmode.presentation.alarms.AlarmRingingActivity")).apply {
            putExtra("extra_alarm_id", alarm.id.toString())
        }
        val showPendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            showIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerTimeInMillis, showPendingIntent)
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
        } catch (e: SecurityException) {
            healthMonitor.logFailure("SecurityException while scheduling alarm clock.")
        }
    }

    fun cancelUserAlarm(alarmId: Long) {
        val requestCode = (alarmId * 100 + 5).toInt()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.action = AlarmReceiver.ACTION_ALARM_RING
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
