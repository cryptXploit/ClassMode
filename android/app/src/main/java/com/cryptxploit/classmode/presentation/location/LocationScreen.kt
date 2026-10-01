package com.cryptxploit.classmode.presentation.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.cryptxploit.classmode.presentation.components.DestructiveConfirmationDialog
import com.cryptxploit.classmode.presentation.components.EmptyStateView
import com.cryptxploit.classmode.presentation.theme.LocalHaptic
import com.cryptxploit.classmode.R

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import android.preference.PreferenceManager
import com.cryptxploit.classmode.data.local.entity.GeofenceEntity
import androidx.compose.foundation.clickable
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    geofences: List<GeofenceEntity>,
    onAddGeofence: (Double, Double, Float) -> Unit,
    onDeleteGeofence: (GeofenceEntity) -> Unit
) {
    var geofenceToDelete by remember { mutableStateOf<GeofenceEntity?>(null) }
    val haptic = LocalHaptic.current
    var isAddingNew by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    var hasFineLocation by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
    var hasBackgroundLocation by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }
    
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasFineLocation = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
    }

    val backgroundPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasBackgroundLocation = isGranted
    }

    LaunchedEffect(hasFineLocation) {
        if (!hasFineLocation) {
            locationPermissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ))
        } else if (!hasBackgroundLocation && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            backgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    if (isAddingNew) {
        FullScreenMapSelector(
            onDismiss = { isAddingNew = false },
            onSave = { lat, lon, rad ->
                onAddGeofence(lat, lon, rad)
                isAddingNew = false
            }
        )
    } else {
        Scaffold(
            topBar = { TopAppBar(title = { Text(androidx.compose.ui.res.stringResource(com.cryptxploit.classmode.R.string.title_locations)) }) },
            floatingActionButton = {
                FloatingActionButton(onClick = { 
                    if (!hasFineLocation) {
                        locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                    } else if (!hasBackgroundLocation && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        backgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    } else {
                        isAddingNew = true 
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Location")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (!hasFineLocation || !hasBackgroundLocation) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(
                            text = if (!hasFineLocation) "Location permission is required." else "Background location ('Allow all the time') is required for automatic geofencing. Please tap the + button.",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }

                if (geofences.isEmpty()) {
                    EmptyStateView(
                        title = stringResource(R.string.title_locations),
                        subtitle = stringResource(R.string.msg_no_locations),
                        icon = Icons.Default.Place,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(geofences) { geofence ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { haptic.performClickEffect() }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(androidx.compose.ui.res.stringResource(com.cryptxploit.classmode.R.string.label_classroom_rule, geofence.ruleId), style = MaterialTheme.typography.titleMedium)
                                        Text(androidx.compose.ui.res.stringResource(com.cryptxploit.classmode.R.string.label_radius, geofence.radiusMeters.toInt()), style = MaterialTheme.typography.bodyMedium)
                                    }
                                    IconButton(onClick = { geofenceToDelete = geofence }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    DestructiveConfirmationDialog(
        showDialog = geofenceToDelete != null,
        title = stringResource(R.string.title_warning),
        text = stringResource(R.string.msg_delete_location),
        onConfirm = {
            geofenceToDelete?.let { onDeleteGeofence(it) }
            geofenceToDelete = null
        },
        onDismiss = { geofenceToDelete = null }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenMapSelector(
    onDismiss: () -> Unit,
    onSave: (Double, Double, Float) -> Unit
) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf<GeoPoint?>(null) }
    var radiusMeters by remember { mutableStateOf(100f) }
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var initialLocationFound by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = context.packageName
        
        // Try to get current location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null && !initialLocationFound) {
                    val currentPoint = GeoPoint(location.latitude, location.longitude)
                    selectedLocation = currentPoint
                    initialLocationFound = true
                    mapView?.controller?.setCenter(currentPoint)
                    mapView?.controller?.setZoom(17.0)
                }
            }
        } catch (e: SecurityException) {
            // Permission missing
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)
                    
                    // Fallback to Dhaka if location fails
                    if (!initialLocationFound) {
                        controller.setCenter(GeoPoint(23.8103, 90.4125))
                    }
                    mapView = this
                    
                    val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                            p?.let { selectedLocation = it }
                            return true
                        }
                        override fun longPressHelper(p: GeoPoint?): Boolean = false
                    })
                    overlays.add(mapEventsOverlay)
                }
            },
            update = { view ->
                // Remove dynamic overlays only
                view.overlays.removeAll { it is Polygon || it is Marker }

                // Add Marker and Circle
                selectedLocation?.let { point ->
                    val circle = Polygon(view)
                    circle.points = Polygon.pointsAsCircle(point, radiusMeters.toDouble())
                    circle.fillColor = 0x330000FF // Translucent blue
                    circle.strokeColor = 0xFF0000FF.toInt()
                    circle.strokeWidth = 2f
                    view.overlays.add(circle)

                    val marker = Marker(view)
                    marker.position = point
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    view.overlays.add(marker)
                }
                view.invalidate()
            },
            modifier = Modifier.fillMaxSize()
        )

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SmallFloatingActionButton(onClick = onDismiss, containerColor = MaterialTheme.colorScheme.surface) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
            
            SmallFloatingActionButton(onClick = {
                try {
                    LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener { loc ->
                        loc?.let {
                            val p = GeoPoint(it.latitude, it.longitude)
                            selectedLocation = p
                            mapView?.controller?.animateTo(p)
                        }
                    }
                } catch(e: SecurityException){}
            }, containerColor = MaterialTheme.colorScheme.surface) {
                Icon(Icons.Default.Place, "Current Location")
            }
        }

        // Bottom control sheet
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(androidx.compose.ui.res.stringResource(com.cryptxploit.classmode.R.string.title_select_radius), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val options = listOf(100f to "100m", 500f to "500m", 1000f to "1km")
                    options.forEach { (value, label) ->
                        FilterChip(
                            selected = radiusMeters == value,
                            onClick = { radiusMeters = value },
                            label = { Text(label) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        selectedLocation?.let {
                            onSave(it.latitude, it.longitude, radiusMeters)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedLocation != null
                ) {
                    Text(if (selectedLocation != null) androidx.compose.ui.res.stringResource(com.cryptxploit.classmode.R.string.action_set_location) else androidx.compose.ui.res.stringResource(com.cryptxploit.classmode.R.string.msg_tap_map))
                }
            }
        }
    }
}




