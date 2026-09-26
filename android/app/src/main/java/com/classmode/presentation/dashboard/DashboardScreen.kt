package com.classmode.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.classmode.domain.model.SoundProfile
import com.classmode.presentation.components.BannerAd

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val effectiveProfile by viewModel.effectiveProfile.collectAsStateWithLifecycle()
    val contextSnapshot by viewModel.contextSnapshot.collectAsStateWithLifecycle()
    val healthStatus by viewModel.healthStatus.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", fontWeight = FontWeight.Bold) },
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
            
            BannerAd()
            
            // Health Warning
            AnimatedVisibility(
                visible = !healthStatus.isHealthy || contextSnapshot?.isLocationUnavailable == true,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = "Warning", tint = MaterialTheme.colorScheme.onErrorContainer)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (contextSnapshot?.isLocationUnavailable == true) "Location condition could not be verified. Please enable location services." else (healthStatus.lastError ?: "System issue"),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Current Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when(effectiveProfile) {
                        SoundProfile.SILENT -> Color(0xFFE57373) // Red-ish for silent
                        SoundProfile.VIBRATE -> Color(0xFFFFB74D) // Orange for vibrate
                        SoundProfile.NORMAL -> MaterialTheme.colorScheme.primaryContainer
                        SoundProfile.DND -> Color(0xFF9575CD) // Purple for DND
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = if (contextSnapshot?.userOverride != null) 1.15f else 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(targetState = effectiveProfile, transitionSpec = { fadeIn() togetherWith fadeOut() }) { profile ->
                            Icon(
                                imageVector = when (profile) {
                                    SoundProfile.SILENT -> Icons.Default.Warning
                                    SoundProfile.VIBRATE -> Icons.Default.Notifications
                                    SoundProfile.DND -> Icons.Default.Lock
                                    else -> Icons.Default.CheckCircle
                                },
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = Color.White
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Current Mode",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = effectiveProfile.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    if (contextSnapshot?.userOverride != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        SuggestionChip(
                            onClick = { viewModel.clearOverride() },
                            label = { Text("Manual Override Active") },
                            icon = { Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp)) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                labelColor = Color.White,
                                iconContentColor = Color.White
                            ),
                            border = null
                        )
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Silent",
                    isSelected = contextSnapshot?.userOverride == SoundProfile.SILENT,
                    onClick = { viewModel.setTemporaryOverride(SoundProfile.SILENT) }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Vibrate",
                    isSelected = contextSnapshot?.userOverride == SoundProfile.VIBRATE,
                    onClick = { viewModel.setTemporaryOverride(SoundProfile.VIBRATE) }
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    title = "Normal",
                    isSelected = contextSnapshot?.userOverride == SoundProfile.NORMAL,
                    onClick = { viewModel.setTemporaryOverride(SoundProfile.NORMAL) }
                )
            }
            
            // Active Context Info
            if (contextSnapshot?.activeSessions?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Active Schedule",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "  ${contextSnapshot?.activeSessions?.firstOrNull()?.type?.name} Mode",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
