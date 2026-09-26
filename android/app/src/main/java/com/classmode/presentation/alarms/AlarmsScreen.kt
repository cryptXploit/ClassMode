package com.classmode.presentation.alarms

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import com.classmode.presentation.components.EmptyState
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.classmode.domain.model.AlarmDomainModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsScreen(viewModel: AlarmViewModel) {
    val alarms by viewModel.alarms.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Alarm")
            }
        }
    ) { padding ->
        if (alarms.isEmpty()) {
            EmptyState(
                icon = Icons.Default.Notifications,
                title = "No Alarms Set",
                description = "Tap the + button to add a new alarm.",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(alarms) { alarm ->
                    AlarmItem(
                        alarm = alarm,
                        onToggle = { viewModel.toggleAlarm(alarm, it) },
                        onDelete = { viewModel.deleteAlarm(alarm) }
                    )
                }
            }
        }
        
        if (showAddDialog) {
            AddAlarmDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { timeMins, days, label, vib, snooze ->
                    viewModel.addAlarm(timeMins, days, label, vib, snooze)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun AlarmItem(alarm: AlarmDomainModel, onToggle: (Boolean) -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                val hour = alarm.timeMins / 60
                val min = alarm.timeMins % 60
                val amPm = if (hour >= 12) "PM" else "AM"
                val h12 = if (hour % 12 == 0) 12 else hour % 12
                Text(
                    text = String.format("%02d:%02d %s", h12, min, amPm),
                    style = MaterialTheme.typography.headlineMedium
                )
                if (alarm.label.isNotEmpty()) {
                    Text(text = alarm.label, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
                Switch(checked = alarm.isEnabled, onCheckedChange = onToggle)
            }
        }
    }
}

@Composable
fun AddAlarmDialog(onDismiss: () -> Unit, onAdd: (Int, Int, String, Boolean, Int) -> Unit) {
    // Simplified dialog for adding an alarm
    var hour by remember { mutableStateOf(8) }
    var minute by remember { mutableStateOf(0) }
    var label by remember { mutableStateOf("Wake up") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Alarm") },
        text = {
            Column {
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label") }
                )
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly) {
                    // Quick mock for hour/minute selector
                    OutlinedButton(onClick = { hour = (hour + 1) % 24 }) { Text("$hour h") }
                    OutlinedButton(onClick = { minute = (minute + 15) % 60 }) { Text("$minute m") }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAdd(hour * 60 + minute, 0, label, true, 5)
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
