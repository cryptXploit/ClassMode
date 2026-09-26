package com.classmode.presentation.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import com.classmode.presentation.components.EmptyState
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.classmode.data.local.entity.GeofenceEntity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    geofences: List<GeofenceEntity>,
    onAddGeofence: (Double, Double, Float) -> Unit,
    onDeleteGeofence: (GeofenceEntity) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    var hasFineLocation by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
    
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasFineLocation) {
            val permissionsToRequest = mutableListOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            locationPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Classroom Locations") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Location")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!hasFineLocation) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Text(
                        text = "Location permissions are required to use Geofencing.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (geofences.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No locations configured.")
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(geofences) { geofence ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Rule ID: ${geofence.ruleId}", style = MaterialTheme.typography.titleMedium)
                                    Text("Lat: ${geofence.latitude}, Lon: ${geofence.longitude}", style = MaterialTheme.typography.bodyMedium)
                                    Text("Radius: ${geofence.radiusMeters}m", style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = { onDeleteGeofence(geofence) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddGeofenceDialog(
            onDismiss = { showDialog = false },
            onConfirm = { lat, lon, rad ->
                onAddGeofence(lat, lon, rad)
                showDialog = false
            }
        )
    }
}

@Composable
fun AddGeofenceDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double, Double, Float) -> Unit
) {
    var latText by remember { mutableStateOf("0.0") }
    var lonText by remember { mutableStateOf("0.0") }
    var radText by remember { mutableStateOf("100") }
    
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = context.packageName
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Classroom Location") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Simplified MapView (No zoom controls to keep it simple, just tap to place marker)
                Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    AndroidView(
                        factory = { ctx ->
                            MapView(ctx).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(true)
                                controller.setZoom(15.0)
                                val initialPoint = GeoPoint(0.0, 0.0)
                                controller.setCenter(initialPoint)
                                
                                val marker = Marker(this)
                                marker.position = initialPoint
                                marker.title = "Classroom"
                                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                overlays.add(marker)
                            }
                        },
                        update = { mapView ->
                            // Here you'd typically handle map tap events to update latText/lonText
                            // But for this initial version, we rely on manual entry as fallback
                        }
                    )
                }

                OutlinedTextField(
                    value = latText,
                    onValueChange = { latText = it },
                    label = { Text("Latitude") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = lonText,
                    onValueChange = { lonText = it },
                    label = { Text("Longitude") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = radText,
                    onValueChange = { radText = it },
                    label = { Text("Radius (meters)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val lat = latText.toDoubleOrNull()
                    val lon = lonText.toDoubleOrNull()
                    val rad = radText.toFloatOrNull()
                    if (lat != null && lon != null && rad != null) {
                        onConfirm(lat, lon, rad)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
