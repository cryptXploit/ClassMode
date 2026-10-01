package com.cryptxploit.classmode.data.system

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.cryptxploit.classmode.R

class SystemNotificationManager(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID_AUTOMATION = "automation_status_channel"
        const val CHANNEL_ID_REMINDERS = "reminders_channel"
        const val CHANNEL_ID_FOCUS = "focus_sessions_channel"
        const val CHANNEL_ID_ALARMS = "alarm_channel"
        const val CHANNEL_ID_PROBLEMS = "problem_channel"

        const val NOTIFICATION_ID_STATUS = 1001
        const val NOTIFICATION_ID_REMINDER = 1002
        const val NOTIFICATION_ID_FOCUS = 1003
        const val NOTIFICATION_ID_ALARM = 1004
        const val NOTIFICATION_ID_PROBLEM = 1005
    }

    init {
        createChannels()
    }

    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val automationChannel = NotificationChannel(
                CHANNEL_ID_AUTOMATION,
                context.getString(R.string.channel_automation_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = context.getString(R.string.channel_automation_desc)
            }

            val remindersChannel = NotificationChannel(
                CHANNEL_ID_REMINDERS,
                context.getString(R.string.channel_reminders_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            val focusChannel = NotificationChannel(
                CHANNEL_ID_FOCUS,
                context.getString(R.string.channel_focus_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val alarmChannel = NotificationChannel(
                CHANNEL_ID_ALARMS,
                context.getString(R.string.channel_alarms_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            val problemChannel = NotificationChannel(
                CHANNEL_ID_PROBLEMS,
                context.getString(R.string.channel_problems_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannels(listOf(
                automationChannel, remindersChannel, focusChannel, alarmChannel, problemChannel
            ))
        }
    }

    fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    fun showAutomationStatus(title: String, message: String): Boolean {
        if (!hasNotificationPermission()) return false

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_AUTOMATION)
            .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
            .setContentTitle(title)
            .setContentText(message)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()

        notificationManager.notify(NOTIFICATION_ID_STATUS, notification)
        return true
    }

    fun showFocusSessionStatus(title: String, message: String): Boolean {
        if (!hasNotificationPermission()) return false

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_FOCUS)
            .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
            .setContentTitle(title)
            .setContentText(message)
            .setOngoing(true)
            .setAutoCancel(false)
            .build()

        notificationManager.notify(NOTIFICATION_ID_FOCUS, notification)
        return true
    }

    fun cancelAutomationStatus() {
        notificationManager.cancel(NOTIFICATION_ID_STATUS)
        notificationManager.cancel(NOTIFICATION_ID_FOCUS)
    }

    fun showProblemNotification(title: String, message: String): Boolean {
        if (!hasNotificationPermission()) return false

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_PROBLEMS)
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setContentTitle(title)
            .setContentText(message)
            .setOngoing(false)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID_PROBLEM, notification)
        return true
    }
    
    fun cancelProblemNotification() {
        notificationManager.cancel(NOTIFICATION_ID_PROBLEM)
    }

    fun showReminderNotification(title: String, message: String): Boolean {
        if (!hasNotificationPermission()) return false

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(message)
            .setOngoing(false)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID_REMINDER, notification)
        return true
    }
}

