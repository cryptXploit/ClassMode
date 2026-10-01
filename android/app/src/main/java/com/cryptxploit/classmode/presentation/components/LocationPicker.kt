package com.cryptxploit.classmode.presentation.components

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.cryptxploit.classmode.presentation.theme.LocalHaptic
import com.cryptxploit.classmode.R
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

@Composable
fun LocationPicker(
    currentLat: Double?,
    currentLng: Double?,
    onLocationSelected: (Double, Double) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHaptic.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Reverse geocode initial coordinates if they exist
    LaunchedEffect(currentLat, currentLng) {
        if (currentLat != null && currentLng != null) {
            coroutineScope.launch {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = withContext(Dispatchers.IO) {
                        geocoder.getFromLocation(currentLat, currentLng, 1)
                    }
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val feature = address.featureName ?: ""
                        val locality = address.locality ?: ""
                        val subAdmin = address.subAdminArea ?: ""
                        val admin = address.adminArea ?: ""
                        
                        val components = listOf(feature, locality, subAdmin, admin)
                            .filter { it.isNotBlank() }
                            .distinct()
                        
                        val addressText = if (components.isNotEmpty()) {
                            components.joinToString(", ")
                        } else {
                            address.getAddressLine(0) ?: "$currentLat, $currentLng"
                        }
                        
                        if (searchQuery.isBlank() || searchQuery != addressText) {
                            searchQuery = addressText
                        }
                    } else if (searchQuery.isBlank()) {
                        searchQuery = "$currentLat, $currentLng"
                    }
                } catch (e: Exception) {
                    if (searchQuery.isBlank()) {
                        searchQuery = "$currentLat, $currentLng"
                    }
                }
            }
        }
    }

    val performSearch = {
        if (searchQuery.isNotBlank()) {
            isLoading = true
            focusManager.clearFocus()
            coroutineScope.launch {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = withContext(Dispatchers.IO) {
                        geocoder.getFromLocationName(searchQuery, 1)
                    }
                    if (!addresses.isNullOrEmpty()) {
                        val location = addresses[0]
                        haptic.performClickEffect()
                        onLocationSelected(location.latitude, location.longitude)
                    } else {
                        Toast.makeText(context, context.getString(R.string.msg_location_not_found), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    Toast.makeText(context, context.getString(R.string.msg_location_not_found), Toast.LENGTH_SHORT).show()
                } finally {
                    isLoading = false
                }
            }
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.any { it.value }
        if (granted) {
            isLoading = true
            try {
                LocationServices.getFusedLocationProviderClient(context)
                    .lastLocation
                    .addOnSuccessListener { loc ->
                        if (loc != null) {
                            haptic.performClickEffect()
                            onLocationSelected(loc.latitude, loc.longitude)
                        } else {
                            Toast.makeText(context, context.getString(R.string.msg_location_not_found), Toast.LENGTH_SHORT).show()
                        }
                        isLoading = false
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, context.getString(R.string.msg_location_not_found), Toast.LENGTH_SHORT).show()
                        isLoading = false
                    }
            } catch (e: SecurityException) {
                isLoading = false
            }
        } else {
            Toast.makeText(context, context.getString(R.string.msg_permission_denied), Toast.LENGTH_SHORT).show()
        }
    }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text(context.getString(R.string.hint_search_location)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { performSearch() }),
        trailingIcon = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp).padding(end = 8.dp), strokeWidth = 2.dp)
                }
                IconButton(onClick = performSearch) {
                    Icon(Icons.Default.Search, contentDescription = "Search Location")
                }
                IconButton(
                    onClick = {
                        val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        
                        if (hasFine || hasCoarse) {
                            haptic.performClickEffect()
                            isLoading = true
                            try {
                                LocationServices.getFusedLocationProviderClient(context)
                                    .lastLocation
                                    .addOnSuccessListener { loc ->
                                        if (loc != null) {
                                            haptic.performClickEffect()
                                            onLocationSelected(loc.latitude, loc.longitude)
                                        } else {
                                            Toast.makeText(context, context.getString(R.string.msg_location_not_found), Toast.LENGTH_SHORT).show()
                                        }
                                        isLoading = false
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context, context.getString(R.string.msg_location_not_found), Toast.LENGTH_SHORT).show()
                                        isLoading = false
                                    }
                            } catch (e: SecurityException) {
                                isLoading = false
                            }
                        } else {
                            locationPermissionLauncher.launch(arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ))
                        }
                    }
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Auto-Capture GPS")
                }
            }
        }
    )
}
