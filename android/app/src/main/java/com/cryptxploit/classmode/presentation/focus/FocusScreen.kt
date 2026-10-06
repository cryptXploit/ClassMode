package com.cryptxploit.classmode.presentation.focus

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.cryptxploit.classmode.R
import com.cryptxploit.classmode.presentation.components.ClassModeCard
import com.cryptxploit.classmode.presentation.theme.ClassModeTheme
import com.cryptxploit.classmode.presentation.theme.LocalHaptic
import com.cryptxploit.classmode.presentation.theme.pressClickEffect
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(viewModel: FocusViewModel, navController: NavController) {
    val activeFocusSession by viewModel.activeFocusSession.collectAsStateWithLifecycle()
    val haptic = LocalHaptic.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_focus_mode), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    val backInteractionSource = remember { MutableInteractionSource() }
                    IconButton(
                        onClick = { 
                            haptic.performClickEffect()
                            navController.popBackStack() 
                        },
                        interactionSource = backInteractionSource,
                        modifier = Modifier.pressClickEffect(backInteractionSource)
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.action_cancel))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (activeFocusSession != null) {
                val session = activeFocusSession!!
                
                val endTime = remember(session) {
                    val cal = Calendar.getInstance()
                    var eMins = session.endTimeMins
                    if (eMins >= 24 * 60) {
                        cal.add(Calendar.DAY_OF_YEAR, 1)
                        eMins -= 24 * 60
                    }
                    cal.set(Calendar.HOUR_OF_DAY, eMins / 60)
                    cal.set(Calendar.MINUTE, eMins % 60)
                    cal.set(Calendar.SECOND, 0)
                    cal.timeInMillis
                }
                
                val totalDurationMillis = remember(session) {
                    (session.endTimeMins - session.startTimeMins) * 60 * 1000L
                }

                var timeLeftMillis by remember { mutableStateOf(endTime - System.currentTimeMillis()) }

                LaunchedEffect(endTime) {
                    while (timeLeftMillis > 0) {
                        timeLeftMillis = endTime - System.currentTimeMillis()
                        delay(50)
                    }
                    if (timeLeftMillis <= 0) {
                        viewModel.stopFocusSession()
                    }
                }
                
                val progress = (timeLeftMillis.toFloat() / totalDurationMillis.toFloat()).coerceIn(0f, 1f)
                
                val animatedProgress by animateFloatAsState(
                    targetValue = progress,
                    animationSpec = tween(durationMillis = 50, easing = LinearEasing)
                )

                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.title_focus_mode),
                        style = MaterialTheme.typography.titleLarge,
                        color = ClassModeTheme.semanticColors.statusDnd,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        val trackColor = MaterialTheme.colorScheme.surfaceVariant
                        val progressColor = ClassModeTheme.semanticColors.statusDnd
                        
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = trackColor,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                            )
                            drawArc(
                                color = progressColor,
                                startAngle = -90f,
                                sweepAngle = animatedProgress * 360f,
                                useCenter = false,
                                style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        
                        val totalSecs = maxOf(0, (timeLeftMillis / 1000).toInt())
                        val h = totalSecs / 3600
                        val m = (totalSecs % 3600) / 60
                        val s = totalSecs % 60
                        
                        val timeString = if (h > 0) {
                            String.format("%d:%02d:%02d", h, m, s)
                        } else {
                            String.format("%02d:%02d", m, s)
                        }
                        
                        Text(
                            text = timeString,
                            fontSize = if (h > 0) 56.sp else 72.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground,
                            letterSpacing = (-1).sp
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    val stopInteractionSource = remember { MutableInteractionSource() }
                    Button(
                        onClick = { 
                            haptic.performClickEffect()
                            viewModel.stopFocusSession() 
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .height(56.dp)
                            .pressClickEffect(stopInteractionSource),
                        interactionSource = stopInteractionSource,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.action_cancel), 
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.title_focus_mode),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Silence notifications and calls for a set duration to maintain focus.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    ClassModeCard(
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = ClassModeTheme.semanticColors.surfaceElevated
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Select Duration",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                val minutes = listOf(25, 50, 90)
                                minutes.forEach { min ->
                                    val buttonInteractionSource = remember { MutableInteractionSource() }
                                    Button(
                                        onClick = { 
                                            haptic.performClickEffect()
                                            viewModel.startFocusSession(min) 
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 4.dp)
                                            .height(56.dp)
                                            .pressClickEffect(buttonInteractionSource),
                                        interactionSource = buttonInteractionSource,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = "${min}m",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
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
}
