package com.cryptxploit.classmode.presentation.schedules

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
import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import com.cryptxploit.classmode.domain.model.AutomationRuleCondition
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import com.cryptxploit.classmode.presentation.components.DestructiveConfirmationDialog
import com.cryptxploit.classmode.presentation.components.EmptyStateView
import com.cryptxploit.classmode.presentation.theme.LocalHaptic
import com.cryptxploit.classmode.presentation.theme.ClassModeTheme
import com.cryptxploit.classmode.presentation.theme.pressClickEffect
import com.cryptxploit.classmode.presentation.components.ClassModeCard
import com.cryptxploit.classmode.R
import androidx.compose.ui.res.stringResource
import com.cryptxploit.classmode.presentation.components.LocationPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulesScreen(viewModel: ScheduleViewModel) {
    val schedules by viewModel.schedules.collectAsStateWithLifecycle(initialValue = emptyList())
    var showEditorDialog by remember { mutableStateOf(false) }
    var scheduleToEdit by remember { mutableStateOf<ScheduleEntity?>(null) }
    var scheduleToDelete by remember { mutableStateOf<ScheduleEntity?>(null) }
    val haptic = LocalHaptic.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_schedules), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            val fabInteractionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
            FloatingActionButton(
                onClick = {
                    haptic.performClickEffect()
                    scheduleToEdit = null
                    showEditorDialog = true
                },
                interactionSource = fabInteractionSource,
                modifier = Modifier.pressClickEffect(fabInteractionSource),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Schedule")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        if (schedules.isEmpty()) {
            EmptyStateView(
                title = stringResource(R.string.title_schedules),
                subtitle = stringResource(R.string.msg_no_upcoming),
                icon = Icons.Default.DateRange,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp)
            ) {
                items(schedules, key = { it.id }) { schedule ->
                    ScheduleItemCard(
                        schedule = schedule,
                        onToggle = { isActive -> 
                            haptic.performClickEffect()
                            viewModel.toggleSchedule(schedule, isActive) 
                        },
                        onEdit = {
                            haptic.performClickEffect()
                            scheduleToEdit = schedule
                            showEditorDialog = true
                        },
                        onDelete = { 
                            haptic.performClickEffect()
                            scheduleToDelete = schedule 
                        }
                    )
                }
            }
        }
    }
    
    DestructiveConfirmationDialog(
        showDialog = scheduleToDelete != null,
        title = stringResource(R.string.title_delete_schedule),
        text = stringResource(R.string.msg_delete_schedule, scheduleToDelete?.title?.takeIf { it.isNotBlank() } ?: scheduleToDelete?.type?.name ?: ""),
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
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    
    val containerColor = if (schedule.isEnabled) ClassModeTheme.semanticColors.surfaceElevated 
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    
    val semanticStatusColor = when (schedule.soundProfile) {
        SoundProfile.SILENT -> ClassModeTheme.semanticColors.statusSilent
        SoundProfile.VIBRATE -> ClassModeTheme.semanticColors.statusVibrate
        SoundProfile.DND -> ClassModeTheme.semanticColors.statusDnd
        SoundProfile.NORMAL -> ClassModeTheme.semanticColors.statusNormal
    }
    
    val borderStroke = if (schedule.isEnabled) androidx.compose.foundation.BorderStroke(1.dp, semanticStatusColor.copy(alpha = 0.3f)) else null

    ClassModeCard(
        modifier = Modifier
            .fillMaxWidth()
            .pressClickEffect(interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onEdit,
                onClickLabel = "Edit Schedule"
            ),
        containerColor = containerColor,
        border = borderStroke
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (schedule.title.isNotBlank()) schedule.title else schedule.type.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (schedule.isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = (if (schedule.isEnabled) semanticStatusColor else MaterialTheme.colorScheme.onSurfaceVariant).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = schedule.soundProfile.name,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (schedule.isEnabled) semanticStatusColor else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = schedule.condition.name.replace("_", " "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val startHour = schedule.startTimeMins / 60
                    val startMin = schedule.startTimeMins % 60
                    val endHour = schedule.endTimeMins / 60
                    val endMin = schedule.endTimeMins % 60
                    Text(
                        text = String.format("%02d:%02d - %02d:%02d", startHour, startMin, endHour, endMin),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium,
                        color = if (schedule.isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = schedule.isEnabled,
                    onCheckedChange = onToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                        checkedTrackColor = ClassModeTheme.semanticColors.statusActive,
                        uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Days Row
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val days = listOf("S", "M", "T", "W", "T", "F", "S")
                    for (i in 0..6) {
                        val isSelected = (schedule.daysOfWeek and (1 shl i)) != 0
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isSelected && schedule.isEnabled -> ClassModeTheme.semanticColors.statusActive.copy(alpha = 0.15f)
                                        isSelected && !schedule.isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f)
                                        else -> androidx.compose.ui.graphics.Color.Transparent
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = days[i],
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = when {
                                    isSelected && schedule.isEnabled -> ClassModeTheme.semanticColors.statusActive
                                    isSelected && !schedule.isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                }
                            )
                        }
                    }
                }
                
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.action_delete),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
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
    onConfirm: (ScheduleEntity, com.cryptxploit.classmode.data.local.entity.GeofenceEntity?) -> Unit
) {
    val haptic = LocalHaptic.current
    var validationErrorRes by remember { mutableStateOf<Int?>(null) }
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
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (initialSchedule == null) stringResource(R.string.title_new_schedule) else stringResource(R.string.title_edit_schedule),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.label_class_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(stringResource(R.string.label_session_type), style = MaterialTheme.typography.labelLarge)
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
                        Text(String.format(stringResource(R.string.label_start_time), startTimeState.hour, startTimeState.minute))
                    }
                    OutlinedButton(onClick = { showEndTimePicker = true }) {
                        Text(String.format(stringResource(R.string.label_end_time), endTimeState.hour, endTimeState.minute))
                    }
                }
                
                // End time validation check
                val startMins = startTimeState.hour * 60 + startTimeState.minute
                val endMins = endTimeState.hour * 60 + endTimeState.minute
                if (endMins == startMins) {
                    Text(stringResource(R.string.error_time_same), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Text(stringResource(R.string.label_repeat_days), style = MaterialTheme.typography.labelLarge)
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
                    Text(stringResource(R.string.error_no_days), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                Text(stringResource(R.string.label_sound_profile), style = MaterialTheme.typography.labelLarge)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SoundProfile.values().forEach { profile ->
                        FilterChip(
                            selected = soundProfile == profile,
                            onClick = { soundProfile = profile },
                            label = { Text(profile.name) }
                        )
                    }
                }

                Text(stringResource(R.string.label_condition), style = MaterialTheme.typography.labelLarge)
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

                    LocationPicker(
                        currentLat = lat.toDoubleOrNull(),
                        currentLng = lon.toDoubleOrNull(),
                        onLocationSelected = { newLat, newLng ->
                            lat = String.format(java.util.Locale.US, "%.6f", newLat)
                            lon = String.format(java.util.Locale.US, "%.6f", newLng)
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                
                validationErrorRes?.let { errRes ->
                    Text(
                        text = stringResource(errRes),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = { 
                            haptic.performClickEffect()
                            onDismiss() 
                        }
                    ) { 
                        Text(stringResource(R.string.action_cancel)) 
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            haptic.performClickEffect()
                            
                            val isTitleValid = title.isNotBlank()
                            val isTimeValid = endMins != startMins
                            val isDaysValid = selectedDays > 0
                            val isLocationValid = condition == AutomationRuleCondition.TIME_ONLY || (lat.isNotBlank() && lon.isNotBlank() && lat.toDoubleOrNull() != null && lon.toDoubleOrNull() != null)
                            
                            if (!isDaysValid) {
                                validationErrorRes = R.string.error_no_days
                            } else if (!isTimeValid) {
                                validationErrorRes = R.string.error_time_same
                            } else if (!isTitleValid) {
                                validationErrorRes = R.string.label_class_title
                            } else if (!isLocationValid) {
                                validationErrorRes = R.string.error_location_invalid
                            } else {
                                validationErrorRes = null
                                val entity = ScheduleEntity(
                                    id = initialSchedule?.id ?: 0,
                                    type = selectedType,
                                    title = title,
                                    startTimeMins = startMins,
                                    endTimeMins = endMins,
                                    daysOfWeek = selectedDays,
                                    soundProfile = soundProfile,
                                    condition = condition,
                                    isEnabled = initialSchedule?.isEnabled ?: true
                                )
                                onConfirm(entity, if (condition == AutomationRuleCondition.TIME_ONLY) null else com.cryptxploit.classmode.data.local.entity.GeofenceEntity(ruleId = entity.id, latitude = lat.toDouble(), longitude = lon.toDouble(), radiusMeters = 100f))
                            }
                        }
                    ) {
                        Text(stringResource(R.string.action_save))
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
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(R.string.action_ok)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_cancel)) } },
        text = { content() }
    )
}










