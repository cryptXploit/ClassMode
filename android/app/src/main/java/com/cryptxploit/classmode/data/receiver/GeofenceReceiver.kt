package com.cryptxploit.classmode.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.cryptxploit.classmode.ClassModeApplication
import com.cryptxploit.classmode.data.local.entity.TriggerStateEntity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeofenceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e("GeofenceReceiver", "Geofencing error: $errorMessage")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            
            val triggeringGeofences = geofencingEvent.triggeringGeofences ?: return
            
            val app = context.applicationContext as ClassModeApplication
            val triggerStateDao = app.database.triggerStateDao()
            
            // Acquire a WakeLock to ensure the device doesn't sleep before orchestration completes
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ClassMode:GeofenceReceiverWakeLock")
            wakeLock.acquire(10000L) // 10 seconds to allow coroutines and orchestrator to finish
            
            val pendingResult = goAsync()
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    for (geofence in triggeringGeofences) {
                        val ruleIdStr = geofence.requestId
                        val ruleId = ruleIdStr.toLongOrNull() ?: continue
                        
                        val isLocationActive = geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL
                        
                        try {
                            var state = triggerStateDao.getState(ruleId)
                            if (state == null) {
                                state = TriggerStateEntity(ruleId = ruleId, isLocationActive = isLocationActive, lastUpdated = System.currentTimeMillis())
                                triggerStateDao.insertOrUpdate(state)
                            } else {
                                triggerStateDao.updateLocationState(ruleId, isLocationActive, System.currentTimeMillis())
                            }
                        } catch (e: Exception) {
                            Log.e("GeofenceReceiver", "Failed to update location state for $ruleId", e)
                        }
                    }
                } finally {
                    pendingResult.finish()
                    try {
                        if (wakeLock.isHeld) {
                            wakeLock.release()
                        }
                    } catch (e: Exception) {
                        Log.e("GeofenceReceiver", "Error releasing WakeLock", e)
                    }
                }
            }
        } else {
            Log.e("GeofenceReceiver", "Invalid transition type: $geofenceTransition")
        }
    }
}
