package com.cryptxploit.classmode.presentation.alarms

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cryptxploit.classmode.R
import com.cryptxploit.classmode.domain.model.AlarmDomainModel
import com.cryptxploit.classmode.presentation.components.ClassModeCard
import com.cryptxploit.classmode.presentation.components.DestructiveConfirmationDialog
import com.cryptxploit.classmode.presentation.components.EmptyStateView
import com.cryptxploit.classmode.presentation.theme.ClassModeTheme
import com.cryptxploit.classmode.presentation.theme.LocalHaptic
import com.cryptxploit.classmode.presentation.theme.pressClickEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsScreen(viewModel: AlarmViewModel) {
    val haptic = LocalHaptic.current
    val alarms by viewModel.alarms.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var alarmToDelete by remember { mutableStateOf<AlarmDomainModel?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_custom_alarms), fontWeight = FontWeight.Bold) },
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
                    showAddDialog = true 
                },
                interactionSource = fabInteractionSource,
                modifier = Modifier.pressClickEffect(fabInteractionSource),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.title_set_alarm))
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (alarms.isEmpty()) {
            EmptyStateView(
                icon = Icons.Default.Notifications,
                title = stringResource(R.string.title_custom_alarms),
                subtitle = stringResource(R.string.msg_no_alarms),
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(alarms, key = { it.id }) { alarm ->
                    AlarmItemCard(
                        alarm = alarm,
                        onToggle = { 
                            haptic.performClickEffect()
                            viewModel.toggleAlarm(alarm, it) 
                        },
                        onDelete = { 
                            haptic.performClickEffect()
                            alarmToDelete = alarm 
                        }
                    )
                }
            }
        }
        
        if (showAddDialog) {
            AddAlarmDialog(
                onDismiss = { 
                    haptic.performClickEffect()
                    showAddDialog = false 
                },
                onAdd = { timeMins, days, label, vib, snooze ->
                    haptic.performClickEffect()
                    viewModel.addAlarm(timeMins, days, label, vib, snooze)
                    showAddDialog = false
                }
            )
        }
        
        DestructiveConfirmationDialog(
            showDialog = alarmToDelete != null,
            title = stringResource(R.string.title_warning),
            text = stringResource(R.string.msg_delete_alarm),
            onConfirm = {
                alarmToDelete?.let { viewModel.deleteAlarm(it) }
                alarmToDelete = null
            },
            onDismiss = {
                alarmToDelete = null
            }
        )
    }
}

@Composable
fun AlarmItemCard(alarm: AlarmDomainModel, onToggle: (Boolean) -> Unit, onDelete: () -> Unit) {
    val isEnabled = alarm.isEnabled
    val contentAlpha by animateFloatAsState(
        targetValue = if (isEnabled) 1f else 0.5f,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val containerColor = if (isEnabled) ClassModeTheme.semanticColors.surfaceElevated else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val borderStroke = if (isEnabled) androidx.compose.foundation.BorderStroke(1.dp, ClassModeTheme.semanticColors.statusActive.copy(alpha = 0.3f)) else null

    ClassModeCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = containerColor,
        border = borderStroke
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    val hour = alarm.timeMins / 60
                    val min = alarm.timeMins % 60
                    val amPm = if (hour >= 12) "PM" else "AM"
                    val h12 = if (hour % 12 == 0) 12 else hour % 12
                    
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = String.format("%02d:%02d", h12, min),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
                            letterSpacing = (-1).sp
                        )
                        Text(
                            text = " $amPm",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    if (alarm.label.isNotEmpty()) {
                        Text(
                            text = alarm.label, 
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = contentAlpha),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                
                Switch(
                    checked = isEnabled,
                    onCheckedChange = onToggle,
                    modifier = Modifier.scale(1.2f),
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
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val days = listOf("S", "M", "T", "W", "T", "F", "S")
                    for (i in 0..6) {
                        val isDaySelected = (alarm.daysOfWeek and (1 shl i)) != 0
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isDaySelected && isEnabled -> ClassModeTheme.semanticColors.statusActive.copy(alpha = 0.15f)
                                        isDaySelected && !isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f)
                                        else -> Color.Transparent
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = days[i],
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (isDaySelected) FontWeight.Bold else FontWeight.Normal,
                                color = when {
                                    isDaySelected && isEnabled -> ClassModeTheme.semanticColors.statusActive
                                    isDaySelected && !isEnabled -> MaterialTheme.colorScheme.onSurfaceVariant
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                }
                            )
                        }
                    }
                }
                
                val deleteInteractionSource = remember { MutableInteractionSource() }
                IconButton(
                    onClick = onDelete, 
                    modifier = Modifier.size(40.dp).pressClickEffect(deleteInteractionSource),
                    interactionSource = deleteInteractionSource
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, 
                        contentDescription = stringResource(R.string.action_delete), 
                        tint = MaterialTheme.colorScheme.error.copy(alpha = contentAlpha)
                    )
                }
            }
        }
    }
}

@Composable
fun AddAlarmDialog(onDismiss: () -> Unit, onAdd: (Int, Int, String, Boolean, Int) -> Unit) {
    val haptic = LocalHaptic.current
    var label by remember { mutableStateOf("") }
    var hour by remember { mutableStateOf(8) }
    var minute by remember { mutableStateOf(0) }
    var selectedDays by remember { mutableStateOf(0) }
    var vibrate by remember { mutableStateOf(true) }
    
    val context = androidx.compose.ui.platform.LocalContext.current
    val is24Hour = android.text.format.DateFormat.is24HourFormat(context)

    val timePickerDialog = remember {
        android.app.TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
            },
            hour,
            minute,
            is24Hour
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        title = { Text(stringResource(R.string.title_set_alarm), fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val amPm = if (hour >= 12) "PM" else "AM"
                val displayHour = if (is24Hour) hour else if (hour % 12 == 0) 12 else hour % 12
                val timeString = if (is24Hour) {
                    String.format(java.util.Locale.US, "%02d:%02d", hour, minute)
                } else {
                    String.format(java.util.Locale.US, "%02d:%02d %s", displayHour, minute, amPm)
                }

                val timeInteractionSource = remember { MutableInteractionSource() }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pressClickEffect(timeInteractionSource)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable(
                            interactionSource = timeInteractionSource,
                            indication = null
                        ) { 
                            haptic.performClickEffect()
                            timePickerDialog.show() 
                        }
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = timeString,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = (-1).sp
                    )
                }
                
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text(stringResource(R.string.label_alarm_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.label_repeat_days), style = MaterialTheme.typography.labelLarge)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        val days = listOf("S", "M", "T", "W", "T", "F", "S")
                        for (i in 0..6) {
                            val isSelected = (selectedDays and (1 shl i)) != 0
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        haptic.performClickEffect()
                                        selectedDays = if (isSelected) selectedDays and (1 shl i).inv() else selectedDays or (1 shl i)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = days[i], 
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.label_vibrate), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    Switch(
                        checked = vibrate, 
                        onCheckedChange = { 
                            haptic.performClickEffect()
                            vibrate = it 
                        }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    haptic.performClickEffect()
                    val timeMins = hour * 60 + minute
                    onAdd(timeMins, selectedDays, label.ifEmpty { "Alarm" }, vibrate, 5)
                }
            ) { 
                Text(stringResource(R.string.action_save_alarm)) 
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    haptic.performClickEffect()
                    onDismiss()
                }
            ) { 
                Text(stringResource(R.string.action_cancel)) 
            }
        }
    )
}
