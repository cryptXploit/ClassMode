package com.cryptxploit.classmode.presentation.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cryptxploit.classmode.R
import com.cryptxploit.classmode.domain.model.SoundProfile
import com.cryptxploit.classmode.presentation.components.BannerAd
import com.cryptxploit.classmode.presentation.components.ClassModeCard
import com.cryptxploit.classmode.presentation.components.EmptyStateView
import com.cryptxploit.classmode.presentation.theme.ClassModeTheme
import com.cryptxploit.classmode.presentation.theme.LocalHaptic
import com.cryptxploit.classmode.presentation.theme.pressClickEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val effectiveProfile by viewModel.effectiveProfile.collectAsStateWithLifecycle()
    val contextSnapshot by viewModel.contextSnapshot.collectAsStateWithLifecycle()
    val healthStatus by viewModel.healthStatus.collectAsStateWithLifecycle()
    val haptic = LocalHaptic.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_dashboard), fontWeight = FontWeight.Bold) },
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
            Spacer(modifier = Modifier.height(4.dp))
            
            BannerAd()
            
            // Health Warning
            AnimatedVisibility(
                visible = !healthStatus.isHealthy || contextSnapshot?.isLocationUnavailable == true,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                ClassModeCard(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Warning")
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (contextSnapshot?.isLocationUnavailable == true) 
                                "Location condition could not be verified. Please enable location services." 
                                else (healthStatus.lastError ?: "System issue"),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Semantic Status Profile Setup
            val semanticStatusColor = when (effectiveProfile) {
                SoundProfile.SILENT -> ClassModeTheme.semanticColors.statusSilent
                SoundProfile.VIBRATE -> ClassModeTheme.semanticColors.statusVibrate
                SoundProfile.DND -> ClassModeTheme.semanticColors.statusDnd
                SoundProfile.NORMAL -> ClassModeTheme.semanticColors.statusNormal
            }
            
            val statusIcon = when (effectiveProfile) {
                SoundProfile.SILENT -> Icons.Default.Warning
                SoundProfile.VIBRATE -> Icons.Default.Notifications
                SoundProfile.DND -> Icons.Default.Lock
                SoundProfile.NORMAL -> Icons.Default.CheckCircle
            }

            // Current Status Card
            ClassModeCard(
                modifier = Modifier.fillMaxWidth(),
                containerColor = ClassModeTheme.semanticColors.surfaceElevated,
                border = BorderStroke(1.dp, semanticStatusColor.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = if (contextSnapshot?.userOverride != null) 1.05f else 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "override_pulse"
                    )

                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(semanticStatusColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = effectiveProfile, 
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "status_icon"
                        ) { profile ->
                            Icon(
                                imageVector = statusIcon,
                                contentDescription = profile.name,
                                modifier = Modifier.size(36.dp),
                                tint = semanticStatusColor
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = stringResource(R.string.title_dashboard), // Or "Current Mode" if localized string missing
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = effectiveProfile.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    // State label (Manual vs Auto)
                    val isManual = contextSnapshot?.userOverride != null
                    val stateLabelColor = if (isManual) ClassModeTheme.semanticColors.statusVibrate else ClassModeTheme.semanticColors.statusInactive
                    
                    Surface(
                        color = stateLabelColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = if (isManual) "MANUAL OVERRIDE" else "AUTOMATIC",
                            style = MaterialTheme.typography.labelSmall,
                            color = stateLabelColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    if (isManual) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val clearInteractionSource = remember { MutableInteractionSource() }
                        Button(
                            onClick = {
                                haptic.performClickEffect()
                                viewModel.clearOverride()
                            },
                            modifier = Modifier.pressClickEffect(clearInteractionSource),
                            interactionSource = clearInteractionSource,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.action_clear_override))
                        }
                    }
                }
            }

            // Quick Actions
            Text(
                "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                "Instantly force your phone's sound mode.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Silent",
                    isSelected = contextSnapshot?.userOverride == SoundProfile.SILENT,
                    semanticColor = ClassModeTheme.semanticColors.statusSilent,
                    onClick = {
                        haptic.performClickEffect()
                        viewModel.setTemporaryOverride(SoundProfile.SILENT)
                    }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Vibrate",
                    isSelected = contextSnapshot?.userOverride == SoundProfile.VIBRATE,
                    semanticColor = ClassModeTheme.semanticColors.statusVibrate,
                    onClick = {
                        haptic.performClickEffect()
                        viewModel.setTemporaryOverride(SoundProfile.VIBRATE)
                    }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Normal",
                    isSelected = contextSnapshot?.userOverride == SoundProfile.NORMAL,
                    semanticColor = ClassModeTheme.semanticColors.statusNormal,
                    onClick = {
                        haptic.performClickEffect()
                        viewModel.setTemporaryOverride(SoundProfile.NORMAL)
                    }
                )
            }
            
            // Active Context Info
            if (contextSnapshot?.activeSessions?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(8.dp))
                ClassModeCard(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = ClassModeTheme.semanticColors.statusActive.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, ClassModeTheme.semanticColors.statusActive.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Active Schedule",
                            style = MaterialTheme.typography.titleSmall,
                            color = ClassModeTheme.semanticColors.statusActive,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            " Mode",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    semanticColor: Color,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val containerColor = if (isSelected) semanticColor else MaterialTheme.colorScheme.surfaceVariant
    val contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
    
    Button(
        onClick = onClick,
        interactionSource = interactionSource,
        modifier = modifier
            .height(60.dp)
            .pressClickEffect(interactionSource),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
