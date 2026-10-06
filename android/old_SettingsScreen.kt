package com.cryptxploit.classmode.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cryptxploit.classmode.R
import com.cryptxploit.classmode.domain.model.SoundProfile
import com.cryptxploit.classmode.presentation.components.ClassModeCard
import com.cryptxploit.classmode.presentation.components.DestructiveConfirmationDialog
import com.cryptxploit.classmode.presentation.theme.ClassModeTheme
import com.cryptxploit.classmode.presentation.theme.LocalHaptic
import com.cryptxploit.classmode.presentation.theme.pressClickEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val isAutomationEnabled by viewModel.isAutomationEnabled.collectAsStateWithLifecycle()
    val isHapticsEnabled by viewModel.isHapticsEnabled.collectAsStateWithLifecycle()
    val defaultProfile by viewModel.defaultProfile.collectAsStateWithLifecycle()
    val testResult by viewModel.testResult.collectAsStateWithLifecycle()
    val haptic = LocalHaptic.current
    var showClearDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.title_settings), fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                stringResource(id = R.string.nav_others), 
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, bottom = -8.dp)
            )
            
            // Automation Master Switch
            ClassModeCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(id = R.string.title_automation_enabled),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            stringResource(id = R.string.desc_automation_enabled),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isAutomationEnabled,
                        onCheckedChange = { 
                            haptic.performClickEffect()
                            viewModel.toggleAutomation(it) 
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = ClassModeTheme.semanticColors.statusActive,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
            
            // Haptics Switch
            ClassModeCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            stringResource(id = R.string.title_haptic_feedback),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            stringResource(id = R.string.desc_haptic_feedback),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isHapticsEnabled,
                        onCheckedChange = { 
                            haptic.performClickEffect()
                            viewModel.toggleHaptics(it) 
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = ClassModeTheme.semanticColors.statusActive,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }

            // Language Dialog - Removed if not fully implemented in viewmodel (it wasn't shown in the snippet)
            // Or maybe it was there? Wait, the previous code jumped to Factory Reset. 
            // I'll add Factory Reset next.

            // Factory Reset
            ClassModeCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        stringResource(id = R.string.title_storage_repair),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        stringResource(id = R.string.desc_storage_repair),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    val resetInteractionSource = remember { MutableInteractionSource() }
                    Button(
                        onClick = { 
                            haptic.performClickEffect()
                            showClearDialog = true 
                        },
                        modifier = Modifier.pressClickEffect(resetInteractionSource),
                        interactionSource = resetInteractionSource,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(id = R.string.action_factory_reset), fontWeight = FontWeight.Bold)
                    }
                }
            }

            DestructiveConfirmationDialog(
                showDialog = showClearDialog,
                title = stringResource(id = R.string.title_factory_reset),
                text = stringResource(id = R.string.msg_factory_reset_confirm),
                onConfirm = {
                    viewModel.clearAllData()
                    showClearDialog = false
                },
                onDismiss = { showClearDialog = false }
            )

            Text(
                stringResource(id = R.string.title_default_ringer), 
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = -8.dp)
            )
            
            // Default Profile
            ClassModeCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        stringResource(id = R.string.title_default_ringer),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        stringResource(id = R.string.desc_default_ringer),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SoundProfile.values().forEach { profile ->
                            val profileNameRes = when(profile) {
                                SoundProfile.NORMAL -> R.string.profile_normal
                                SoundProfile.VIBRATE -> R.string.profile_vibrate
                                SoundProfile.SILENT -> R.string.profile_silent
                                SoundProfile.DND -> R.string.profile_dnd
                            }
                            FilterChip(
                                selected = defaultProfile == profile,
                                onClick = { 
                                    haptic.performClickEffect()
                                    viewModel.setDefaultProfile(profile) 
                                },
                                label = { Text(stringResource(id = profileNameRes)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }
            
            // Test Profile
            ClassModeCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        stringResource(id = R.string.title_test_profile),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        stringResource(id = R.string.desc_test_profile),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Button grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val nSource = remember { MutableInteractionSource() }
                        Button(
                            onClick = { 
                                haptic.performClickEffect()
                                viewModel.testSoundProfile(SoundProfile.NORMAL) 
                            }, 
                            modifier = Modifier.weight(1f).pressClickEffect(nSource),
                            interactionSource = nSource,
                            shape = RoundedCornerShape(12.dp)
                        ) { Text(stringResource(id = R.string.profile_normal)) }
                        
                        val vSource = remember { MutableInteractionSource() }
                        Button(
                            onClick = { 
                                haptic.performClickEffect()
                                viewModel.testSoundProfile(SoundProfile.VIBRATE) 
                            }, 
                            modifier = Modifier.weight(1f).pressClickEffect(vSource),
                            interactionSource = vSource,
                            shape = RoundedCornerShape(12.dp)
                        ) { Text(stringResource(id = R.string.profile_vibrate)) }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val sSource = remember { MutableInteractionSource() }
                        Button(
                            onClick = { 
                                haptic.performClickEffect()
                                viewModel.testSoundProfile(SoundProfile.SILENT) 
                            }, 
                            modifier = Modifier.weight(1f).pressClickEffect(sSource),
                            interactionSource = sSource,
                            shape = RoundedCornerShape(12.dp)
                        ) { Text(stringResource(id = R.string.profile_silent)) }
                        
                        val dSource = remember { MutableInteractionSource() }
                        Button(
                            onClick = { 
                                haptic.performClickEffect()
                                viewModel.testSoundProfile(SoundProfile.DND) 
                            }, 
                            modifier = Modifier.weight(1f).pressClickEffect(dSource),
                            interactionSource = dSource,
                            shape = RoundedCornerShape(12.dp)
                        ) { Text(stringResource(id = R.string.profile_dnd)) }
                    }
                    
                    testResult?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
            
            // About info
            ClassModeCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Default.Info, contentDescription = "Info", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        stringResource(id = R.string.app_version_name), 
                        style = MaterialTheme.typography.titleMedium, 
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}
