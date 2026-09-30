package com.classmode.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.classmode.ClassModeApplication
import com.classmode.data.system.SystemAlarmScheduler
import com.classmode.data.local.entity.TriggerStateEntity
import com.classmode.data.system.SystemNotificationManager
import com.classmode.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_CLASS_START = "com.classmode.ACTION_CLASS_START"
        const val ACTION_CLASS_END = "com.classmode.ACTION_CLASS_END"
        const val ACTION_CLASS_REMINDER = "com.classmode.ACTION_CLASS_REMINDER"
        const val EXTRA_SESSION_ID = "extra_session_id"
        const val ACTION_ALARM_RING = "com.classmode.ACTION_ALARM_RING"
        const val EXTRA_ALARM_ID = "extra_alarm_id"
        const val ACTION_CLEAR_OVERRIDE = "com.classmode.ACTION_CLEAR_OVERRIDE"
    }

        override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as ClassModeApplication
        val scheduleDao = app.database.scheduleDao()
        val triggerStateDao = app.database.triggerStateDao()
        val alarmScheduler = SystemAlarmScheduler(context, app.healthMonitor)
        
        when (intent.action) {
            ACTION_CLEAR_OVERRIDE -> {
                Log.i("AlarmReceiver", "Triggered manual override clear")
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        app.preferencesManager.setUserOverride(null)
                    } catch (e: Exception) {
                        Log.e("AlarmReceiver", "Failed to clear override", e)
                    }
                }
            }
            ACTION_ALARM_RING -> {
                val alarmIdStr = intent.getStringExtra(EXTRA_ALARM_ID) ?: return
                val alarmId = alarmIdStr.toLongOrNull() ?: return
                val ringIntent = Intent(context, Class.forName("com.classmode.presentation.alarms.AlarmRingingActivity")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    putExtra(EXTRA_ALARM_ID, alarmIdStr)
                }
                
                val fullScreenPendingIntent = android.app.PendingIntent.getActivity(
                    context,
                    (alarmId * 100 + 6).toInt(),
                    ringIntent,
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )

                val notification = androidx.core.app.NotificationCompat.Builder(context, SystemNotificationManager.CHANNEL_ID_ALARMS)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setContentTitle("Alarm Ringing")
                    .setContentText("Tap to open alarm")
                    .setPriority(androidx.core.app.NotificationCompat.PRIORITY_HIGH)
                    .setCategory(androidx.core.app.NotificationCompat.CATEGORY_ALARM)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .setOngoing(true)
                    .setAutoCancel(false)
                    .build()

                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                notificationManager.notify((alarmId * 100).toInt(), notification)
                
                try {
                    context.startActivity(ringIntent)
                } catch (e: Exception) {
                    Log.e("AlarmReceiver", "Could not launch activity", e)
                }
            }
            ACTION_CLASS_START -> {
                val sessionIdStr = intent.getStringExtra(EXTRA_SESSION_ID) ?: return
                val sessionId = sessionIdStr.toLongOrNull() ?: return
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        var state = triggerStateDao.getState(sessionId)
                        if (state == null) {
                            state = TriggerStateEntity(ruleId = sessionId, isTimeActive = true, lastUpdated = System.currentTimeMillis())
                            triggerStateDao.insertOrUpdate(state)
                        } else {
                            triggerStateDao.updateTimeState(sessionId, true, System.currentTimeMillis())
                        }
                    } catch (e: Exception) {
                        Log.e("AlarmReceiver", "Failed to update state for $sessionId", e)
                    }
                }
                reschedule(sessionId, scheduleDao, alarmScheduler)
            }
            ACTION_CLASS_END -> {
                val sessionIdStr = intent.getStringExtra(EXTRA_SESSION_ID) ?: return
                val sessionId = sessionIdStr.toLongOrNull() ?: return
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        triggerStateDao.updateTimeState(sessionId, false, System.currentTimeMillis())
                        val endedSchedule = scheduleDao.getScheduleById(sessionId)
                        if (endedSchedule?.type == com.classmode.domain.model.SessionType.FOCUS) {
                            scheduleDao.deleteSchedule(endedSchedule)
                        }
                    } catch (e: Exception) {
                        Log.e("AlarmReceiver", "Failed to update state for $sessionId", e)
                    }
                }
                reschedule(sessionId, scheduleDao, alarmScheduler)
            }
            ACTION_CLASS_REMINDER -> {
                val sessionIdStr = intent.getStringExtra(EXTRA_SESSION_ID) ?: return
                val sessionId = sessionIdStr.toLongOrNull() ?: return
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val schedule = scheduleDao.getScheduleById(sessionId)
                        if (schedule != null) {
                            val title = context.getString(R.string.status_next_class, 15)
                            val message = schedule.title
                            val notificationManager = SystemNotificationManager(context)
                            notificationManager.showReminderNotification(title, message)
                        }
                    } catch (e: Exception) {
                        Log.e("AlarmReceiver", "Failed to show reminder", e)
                    }
                }
            }
        }
    }

    private fun reschedule(sessionId: Long, scheduleDao: com.classmode.data.local.dao.ScheduleDao, alarmScheduler: SystemAlarmScheduler) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val schedule = scheduleDao.getScheduleById(sessionId)
                if (schedule != null && schedule.isEnabled) {
                    alarmScheduler.scheduleClass(schedule)
                }
            } catch (e: Exception) {
                Log.e("AlarmReceiver", "Failed to reschedule alarm for $sessionId", e)
            }
        }
    }
}





