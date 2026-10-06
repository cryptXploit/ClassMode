package com.cryptxploit.classmode.presentation.location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.preference.PreferenceManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.cryptxploit.classmode.R
import com.cryptxploit.classmode.data.local.entity.GeofenceEntity
import com.cryptxploit.classmode.presentation.components.ClassModeCard
import com.cryptxploit.classmode.presentation.components.DestructiveConfirmationDialog
import com.cryptxploit.classmode.presentation.components.EmptyStateView
import com.cryptxploit.classmode.presentation.components.LocationPicker
import com.cryptxploit.classmode.presentation.theme.ClassModeTheme
import com.cryptxploit.classmode.presentation.theme.LocalHaptic
import com.cryptxploit.classmode.presentation.theme.pressClickEffect
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
            onDismiss = { 
                haptic.performClickEffect()
                isAddingNew = false 
            },
            onSave = { lat, lon, rad ->
                haptic.performClickEffect()
                onAddGeofence(lat, lon, rad)
                isAddingNew = false
            }
        )
    } else {
        Scaffold(
            topBar = { 
                TopAppBar(
                    title = { Text(stringResource(R.string.title_locations), fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                ) 
            },
            floatingActionButton = {
                val fabInteractionSource = remember { MutableInteractionSource() }
                FloatingActionButton(
                    onClick = { 
                        haptic.performClickEffect()
                        if (!hasFineLocation) {
                            locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                        } else if (!hasBackgroundLocation && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            backgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        } else {
                            isAddingNew = true 
                        }
                    },
                    interactionSource = fabInteractionSource,
                    modifier = Modifier.pressClickEffect(fabInteractionSource),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.title_locations))
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (!hasFineLocation || !hasBackgroundLocation) {
                    ClassModeCard(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Place, contentDescription = "Warning", tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = if (!hasFineLocation) "Location permission is required." else "Background location ('Allow all the time') is required for automatic geofencing. Please tap the + button to request it.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
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
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(geofences, key = { it.id }) { geofence ->
                            ClassModeCard(
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = ClassModeTheme.semanticColors.surfaceElevated
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(48.dp)
                                                .clip(CircleShape)
                                                .background(ClassModeTheme.semanticColors.statusActive.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.Place, 
                                                contentDescription = null,
                                                tint = ClassModeTheme.semanticColors.statusActive
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column {
                                            Text(
                                                text = stringResource(R.string.label_classroom_rule, geofence.ruleId), 
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = stringResource(R.string.label_radius, geofence.radiusMeters.toInt()), 
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    
                                    val deleteInteractionSource = remember { MutableInteractionSource() }
                                    IconButton(
                                        onClick = { 
                                            haptic.performClickEffect()
                                            geofenceToDelete = geofence 
                                        },
                                        interactionSource = deleteInteractionSource,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .pressClickEffect(deleteInteractionSource)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete, 
                                            contentDescription = stringResource(R.string.action_delete), 
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                        )
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
        } catch (e: SecurityException) {}
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                MapView(ctx).apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)
                    
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
                view.overlays.removeAll { it is Polygon || it is Marker }

                selectedLocation?.let { point ->
                    val circle = Polygon(view)
                    circle.points = Polygon.pointsAsCircle(point, radiusMeters.toDouble())
                    circle.fillColor = 0x330000FF
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
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val backInteractionSource = remember { MutableInteractionSource() }
            SmallFloatingActionButton(
                onClick = onDismiss, 
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                interactionSource = backInteractionSource,
                modifier = Modifier.pressClickEffect(backInteractionSource)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.action_cancel))
            }
            
            Box(modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))) {
                LocationPicker(
                    currentLat = selectedLocation?.latitude,
                    currentLng = selectedLocation?.longitude,
                    onLocationSelected = { newLat, newLng ->
                        val p = GeoPoint(newLat, newLng)
                        selectedLocation = p
                        mapView?.controller?.animateTo(p)
                        mapView?.controller?.setZoom(17.0)
                    }
                )
            }
        }

        // Bottom control sheet
        ClassModeCard(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            elevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(R.string.title_select_radius), 
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val options = listOf(100f to "100m", 500f to "500m", 1000f to "1km")
                    options.forEach { (value, label) ->
                        FilterChip(
                            selected = radiusMeters == value,
                            onClick = { radiusMeters = value },
                            label = { Text(label, fontWeight = FontWeight.Medium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                val saveInteractionSource = remember { MutableInteractionSource() }
                Button(
                    onClick = {
                        selectedLocation?.let {
                            onSave(it.latitude, it.longitude, radiusMeters)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp).pressClickEffect(saveInteractionSource),
                    enabled = selectedLocation != null,
                    interactionSource = saveInteractionSource,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (selectedLocation != null) stringResource(R.string.action_set_location) else stringResource(R.string.msg_tap_map),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
