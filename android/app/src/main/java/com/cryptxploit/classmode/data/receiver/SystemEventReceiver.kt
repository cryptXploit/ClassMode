package com.cryptxploit.classmode.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.cryptxploit.classmode.ClassModeApplication
import com.cryptxploit.classmode.data.system.SystemAlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Awakens the application safely when the device reboots, updates, or changes timezones.
 * Required to reconstruct AlarmManager schedules without relying on foreground services.
 */
class SystemEventReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED || 
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            
            Log.i("SystemEventReceiver", "Critical system event received: $action. Triggering schedule rebuild.")
            
            val app = context.applicationContext as ClassModeApplication
            val scheduleDao = app.database.scheduleDao()
            val alarmScheduler = SystemAlarmScheduler(context, app.healthMonitor)
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val override = app.preferencesManager.userOverrideFlow.first()
                    val expiryTime = app.preferencesManager.overrideExpiryTimeFlow.first()
                    
                    if (override != null && expiryTime > 0) {
                        if (System.currentTimeMillis() >= expiryTime) {
                            Log.i("SystemEventReceiver", "Override expired while device was off. Clearing.")
                            app.preferencesManager.setUserOverride(null)
                        } else {
                            Log.i("SystemEventReceiver", "Rescheduling active override clear.")
                            alarmScheduler.scheduleOverrideClear(expiryTime)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SystemEventReceiver", "Failed to check override expiry", e)
                }

                try {
                    val schedules = scheduleDao.getAllSchedules().first()
                    for (schedule in schedules) {
                        try {
                            if (schedule.isEnabled) {
                                alarmScheduler.scheduleClass(schedule)
                            } else {
                                alarmScheduler.cancelSchedule(schedule.id)
                            }
                        } catch (e: Exception) {
                            Log.e("SystemEventReceiver", "Failed to process schedule ${schedule.id}", e)
                        }
                    }
                    Log.i("SystemEventReceiver", "Successfully processed alarms for ${schedules.size} schedules.")
                } catch (e: Exception) {
                    Log.e("SystemEventReceiver", "Failed to rebuild alarms", e)
                }
            }
        }
    }
}


