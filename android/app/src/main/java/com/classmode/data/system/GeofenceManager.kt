package com.classmode.data.system

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.classmode.data.local.entity.GeofenceEntity
import com.classmode.data.receiver.GeofenceReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class GeofenceManager(private val context: Context) {
    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    suspend fun registerGeofence(geofenceEntity: GeofenceEntity): Boolean {
        if (!hasLocationPermission()) return false

        val geofence = Geofence.Builder()
            .setRequestId(geofenceEntity.ruleId.toString())
            .setCircularRegion(
                geofenceEntity.latitude,
                geofenceEntity.longitude,
                geofenceEntity.radiusMeters
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        return try {
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).await()
            true
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Failed to register geofence", e)
            false
        }
    }

    suspend fun removeGeofence(ruleId: Long): Boolean {
        return try {
            geofencingClient.removeGeofences(listOf(ruleId.toString())).await()
            true
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Failed to remove geofence", e)
            false
        }
    }
    
    suspend fun removeAllGeofences(): Boolean {
        return try {
            geofencingClient.removeGeofences(geofencePendingIntent).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val backgroundLocation = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        } else true
        return fineLocation && backgroundLocation
    }
}


