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

    fun scheduleClass(schedule: ScheduleEntity): Boolean {
        val nextStart = ScheduleCalculator.getNextTriggerTime(schedule, isStart = true)
        val nextEnd = ScheduleCalculator.getNextTriggerTime(schedule, isStart = false)

        var success = true
        if (nextStart != -1L) {
            if (!setExactAlarm(nextStart, schedule.id, isStart = true)) success = false
        }
        if (nextEnd != -1L) {
            if (!setExactAlarm(nextEnd, schedule.id, isStart = false)) success = false
        }
        return success
    }

    private fun setExactAlarm(timeInMillis: Long, scheduleId: Long, isStart: Boolean): Boolean {
        if (timeInMillis - System.currentTimeMillis() > 15 * 60 * 1000 && isStart) {
            setReminderAlarm(timeInMillis - 15 * 60 * 1000, scheduleId)
        }
        if (!canScheduleExactAlarms()) {
            healthMonitor.logFailure("Cannot schedule exact alarm for schedule `$scheduleId. Permission denied.")
            SystemNotificationManager(context).showProblemNotification("Permission Required", "ClassMode needs permission to schedule exact alarms.")
            return false
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
            return true
        } catch (e: SecurityException) {
            healthMonitor.logFailure("Exact Alarm permission denied")
            return false
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
            android.util.Log.e("SystemAlarmScheduler", "SecurityException: Exact alarm permission missing or revoked during cancellation", e)
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

    fun scheduleUserAlarm(alarm: com.classmode.domain.model.AlarmDomainModel, triggerTimeInMillis: Long): Boolean {
        if (!canScheduleExactAlarms()) {
            SystemNotificationManager(context).showProblemNotification("Permission Required", "ClassMode needs permission to schedule exact alarms.")
            return false
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
            return true
        } catch (e: SecurityException) {
            healthMonitor.logFailure("Exact Alarm permission denied")
            return false
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
    fun scheduleOverrideClear(timeInMillis: Long): Boolean {
        if (!canScheduleExactAlarms()) {
            healthMonitor.logFailure("Cannot schedule override clear. Permission denied.")
            SystemNotificationManager(context).showProblemNotification("Permission Required", "ClassMode needs permission to schedule exact alarms.")
            return false
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.action = AlarmReceiver.ACTION_CLEAR_OVERRIDE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            9999, // Distinct request code for clear override
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
            healthMonitor.logSuccess("Scheduled override clear at $timeInMillis")
            return true
        } catch (e: SecurityException) {
            healthMonitor.logFailure("Exact Alarm permission denied")
            return false
        }
    }

    fun cancelOverrideClear() {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            this.action = AlarmReceiver.ACTION_CLEAR_OVERRIDE
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            9999,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            healthMonitor.logSuccess("Cancelled override clear")
        }
    }
}

