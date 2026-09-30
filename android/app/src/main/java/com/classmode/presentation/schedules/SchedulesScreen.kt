package com.classmode.presentation.schedules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import com.classmode.presentation.components.EmptyState
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.classmode.data.local.entity.ScheduleEntity
import com.classmode.domain.model.AutomationRuleCondition
import com.classmode.domain.model.SessionType
import com.classmode.domain.model.SoundProfile
import com.classmode.presentation.components.DestructiveConfirmationDialog
import com.classmode.presentation.components.EmptyStateView
import com.classmode.presentation.theme.LocalHaptic

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulesScreen(viewModel: ScheduleViewModel) {
    val schedules by viewModel.schedules.collectAsStateWithLifecycle(initialValue = emptyList<ScheduleEntity>())
    var showEditorDialog by remember { mutableStateOf(false) }
    var scheduleToEdit by remember { mutableStateOf<ScheduleEntity?>(null) }
    var scheduleToDelete by remember { mutableStateOf<ScheduleEntity?>(null) }
    val haptic = LocalHaptic.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.title_schedules), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scheduleToEdit = null
                    showEditorDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Schedule")
            }
        }
    ) { paddingValues ->
        if (schedules.isEmpty()) {
            EmptyStateView(
                title = androidx.compose.ui.res.stringResource(com.classmode.R.string.title_schedules),
                subtitle = androidx.compose.ui.res.stringResource(com.classmode.R.string.msg_no_upcoming),
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
            ) {
                items(schedules, key = { it.id }) { schedule ->
                    ScheduleItemCard(
                        schedule = schedule,
                        onToggle = { isActive -> viewModel.toggleSchedule(schedule, isActive) },
                        onEdit = {
                            scheduleToEdit = schedule
                            showEditorDialog = true
                        },
                        onDelete = { scheduleToDelete = schedule }
                    )
                }
            }
        }
    }
    
    DestructiveConfirmationDialog(
        showDialog = scheduleToDelete != null,
        title = androidx.compose.ui.res.stringResource(com.classmode.R.string.title_delete_schedule),
        text = androidx.compose.ui.res.stringResource(com.classmode.R.string.msg_delete_schedule, scheduleToDelete?.title?.takeIf { it.isNotBlank() } ?: scheduleToDelete?.type?.name ?: ""),
        onConfirm = {
            scheduleToDelete?.let { viewModel.deleteSchedule(it) }
            scheduleToDelete = null
        },
        onDismiss = { scheduleToDelete = null }
    )
    
    if (showEditorDialog) {
        ScheduleEditorDialog(
            initialSchedule = scheduleToEdit,
            onDismiss = { showEditorDialog = false },
            onConfirm = { savedSchedule, geofence ->
                viewModel.saveSchedule(savedSchedule, geofence)
                showEditorDialog = false
            }
        )
    }
}

@Composable
fun ScheduleItemCard(
    schedule: ScheduleEntity,
    onToggle: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val haptic = LocalHaptic.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                haptic.performClickEffect()
                onEdit()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (schedule.isEnabled) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (schedule.title.isNotBlank()) schedule.title else schedule.type.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (schedule.isEnabled) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Mode: ${schedule.soundProfile.name} | ${schedule.condition.name.replace("_", " ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (schedule.isEnabled) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    val startHour = schedule.startTimeMins / 60
                    val startMin = schedule.startTimeMins % 60
                    val endHour = schedule.endTimeMins / 60
                    val endMin = schedule.endTimeMins % 60
                    Text(
                        text = String.format("%02d:%02d - %02d:%02d", startHour, startMin, endHour, endMin),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (schedule.isEnabled) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = schedule.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Days Row
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val days = listOf("S", "M", "T", "W", "T", "F", "S")
                    for (i in 0..6) {
                        val isSelected = (schedule.daysOfWeek and (1 shl i)) != 0
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surface
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = days[i],
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
                
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = if (schedule.isEnabled) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = androidx.compose.ui.res.stringResource(com.classmode.R.string.action_delete),
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleEditorDialog(
    initialSchedule: ScheduleEntity?,
    onDismiss: () -> Unit,
    onConfirm: (ScheduleEntity, com.classmode.data.local.entity.GeofenceEntity?) -> Unit
) {
    var selectedType by remember { mutableStateOf(initialSchedule?.type ?: SessionType.CLASS) }
    var selectedDays by remember { mutableStateOf(initialSchedule?.daysOfWeek ?: 62) } // Mon-Fri default
    var soundProfile by remember { mutableStateOf(initialSchedule?.soundProfile ?: SoundProfile.VIBRATE) }
    var condition by remember { mutableStateOf(initialSchedule?.condition ?: AutomationRuleCondition.TIME_ONLY) }
    var title by remember { mutableStateOf(initialSchedule?.title ?: "") }
    var lat by remember { mutableStateOf("") }
    var lon by remember { mutableStateOf("") }

    val startTimeState = rememberTimePickerState(
        initialHour = (initialSchedule?.startTimeMins ?: 540) / 60,
        initialMinute = (initialSchedule?.startTimeMins ?: 540) % 60
    )
    val endTimeState = rememberTimePickerState(
        initialHour = (initialSchedule?.endTimeMins ?: 960) / 60,
        initialMinute = (initialSchedule?.endTimeMins ?: 960) % 60
    )

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (initialSchedule == null) "New Schedule" else "Edit Schedule",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.label_class_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.label_session_type), style = MaterialTheme.typography.labelLarge)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SessionType.values().filter { it != SessionType.MANUAL_OVERRIDE }.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type.name) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = { showStartTimePicker = true }) {
                        Text(String.format(androidx.compose.ui.res.stringResource(com.classmode.R.string.label_start_time), startTimeState.hour, startTimeState.minute))
                    }
                    OutlinedButton(onClick = { showEndTimePicker = true }) {
                        Text(String.format(androidx.compose.ui.res.stringResource(com.classmode.R.string.label_end_time), endTimeState.hour, endTimeState.minute))
                    }
                }
                
                // End time validation check
                val startMins = startTimeState.hour * 60 + startTimeState.minute
                val endMins = endTimeState.hour * 60 + endTimeState.minute
                if (endMins == startMins) {
                    Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.error_time_same), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.label_repeat_days), style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.fillMaxWidth()) {
                    val days = listOf("S", "M", "T", "W", "T", "F", "S")
                    for (i in 0..6) {
                        val isSelected = (selectedDays and (1 shl i)) != 0
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                .clickable {
                                    selectedDays = if (isSelected) selectedDays and (1 shl i).inv() else selectedDays or (1 shl i)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(days[i], color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
                if (selectedDays == 0) {
                    Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.error_no_days), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.label_sound_profile), style = MaterialTheme.typography.labelLarge)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SoundProfile.values().forEach { profile ->
                        FilterChip(
                            selected = soundProfile == profile,
                            onClick = { soundProfile = profile },
                            label = { Text(profile.name) }
                        )
                    }
                }

                Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.label_condition), style = MaterialTheme.typography.labelLarge)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AutomationRuleCondition.values().toList().forEach { cond ->
                        FilterChip(
                            selected = condition == cond,
                            onClick = { condition = cond },
                            label = { Text(cond.name.replace("_", " ")) }
                        )
                    }
                }
                
                if (condition == AutomationRuleCondition.LOCATION_ONLY || condition == AutomationRuleCondition.TIME_AND_LOCATION || condition == AutomationRuleCondition.TIME_OR_LOCATION) {
                    
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                        androidx.compose.ui.viewinterop.AndroidView(
                            factory = { ctx ->
                                org.osmdroid.views.MapView(ctx).apply {
                                    setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
                                    setMultiTouchControls(true)
                                    controller.setZoom(15.0)
                                    controller.setCenter(org.osmdroid.util.GeoPoint(23.8103, 90.4125)) // Default center
                                    
                                    val mapEventsOverlay = org.osmdroid.views.overlay.MapEventsOverlay(object : org.osmdroid.events.MapEventsReceiver {
                                        override fun singleTapConfirmedHelper(p: org.osmdroid.util.GeoPoint?): Boolean {
                                            p?.let {
                                                lat = String.format(java.util.Locale.US, "%.6f", it.latitude)
                                                lon = String.format(java.util.Locale.US, "%.6f", it.longitude)
                                            }
                                            return true
                                        }
                                        override fun longPressHelper(p: org.osmdroid.util.GeoPoint?): Boolean = false
                                    })
                                    overlays.add(mapEventsOverlay)
                                }
                            },
                            update = { view ->
                                // Remove previous markers
                                view.overlays.removeAll { it is org.osmdroid.views.overlay.Marker }
                                
                                val currentLat = lat.toDoubleOrNull()
                                val currentLon = lon.toDoubleOrNull()
                                if (currentLat != null && currentLon != null) {
                                    val point = org.osmdroid.util.GeoPoint(currentLat, currentLon)
                                    val marker = org.osmdroid.views.overlay.Marker(view)
                                    marker.position = point
                                    marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)
                                    view.overlays.add(marker)
                                    view.controller.setCenter(point)
                                }
                                view.invalidate()
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    OutlinedTextField(
                        value = lat,
                        onValueChange = { lat = it },
                        label = { Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.label_latitude)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = lon,
                        onValueChange = { lon = it },
                        label = { Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.label_longitude)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.action_cancel)) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val entity = ScheduleEntity(
                                id = initialSchedule?.id ?: 0,
                                type = selectedType,
                                title = title.ifBlank { selectedType.name },
                                startTimeMins = startTimeState.hour * 60 + startTimeState.minute,
                                endTimeMins = endTimeState.hour * 60 + endTimeState.minute,
                                daysOfWeek = selectedDays,
                                soundProfile = soundProfile,
                                condition = condition,
                                isEnabled = initialSchedule?.isEnabled ?: true
                            )
                            onConfirm(entity, if (condition == AutomationRuleCondition.TIME_ONLY) null else com.classmode.data.local.entity.GeofenceEntity(ruleId = entity.id, latitude = lat.toDouble(), longitude = lon.toDouble(), radiusMeters = 100f))
                        },
                        enabled = selectedDays > 0 && title.isNotBlank() && (endMins != startMins) && (
                            condition == AutomationRuleCondition.TIME_ONLY || (lat.isNotBlank() && lon.isNotBlank() && lat.toDoubleOrNull() != null && lon.toDoubleOrNull() != null)
                        )
                    ) {
                        Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.action_save))
                    }
                }
            }
        }
    }

    if (showStartTimePicker) {
        TimePickerDialog(
            onDismiss = { showStartTimePicker = false },
            onConfirm = { showStartTimePicker = false }
        ) {
            TimePicker(state = startTimeState)
        }
    }

    if (showEndTimePicker) {
        TimePickerDialog(
            onDismiss = { showEndTimePicker = false },
            onConfirm = { showEndTimePicker = false }
        ) {
            TimePicker(state = endTimeState)
        }
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onConfirm) { Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.action_ok)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(androidx.compose.ui.res.stringResource(com.classmode.R.string.action_cancel)) } },
        text = { content() }
    )
}




