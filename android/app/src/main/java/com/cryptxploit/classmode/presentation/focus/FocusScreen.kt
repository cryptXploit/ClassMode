package com.cryptxploit.classmode.presentation.focus

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cryptxploit.classmode.domain.model.SoundProfile
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(viewModel: FocusViewModel) {
    val activeFocusSession by viewModel.activeFocusSession.collectAsStateWithLifecycle()
    
    // iOS Timer colors
    val iosOrange = Color(0xFFFF9F0A)
    val iosDarkGray = Color(0xFF333333)
    val iosLightGray = Color(0xFFAAAAAA)
    
    // Force a dark theme feel for this specific screen
    val isDark = true
    val bgColor = if (isDark) Color.Black else MaterialTheme.colorScheme.background
    val textColor = if (isDark) Color.White else MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        if (activeFocusSession != null) {
            val session = activeFocusSession!!
            
            // Calculate precise end time
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
                    delay(50) // High refresh rate for smooth progress
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
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Timer Circle
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Background track
                        drawArc(
                            color = iosDarkGray,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        )
                        // Foreground progress
                        drawArc(
                            color = iosOrange,
                            startAngle = -90f,
                            sweepAngle = animatedProgress * 360f,
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    
                    // Time text
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
                        fontSize = if (h > 0) 64.sp else 80.sp,
                        fontWeight = FontWeight.Light,
                        color = textColor,
                        letterSpacing = (-2).sp
                    )
                }
                
                Spacer(modifier = Modifier.height(64.dp))
                
                // iOS style Cancel button
                Row(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IosCircularButton(
                        text = "Cancel",
                        backgroundColor = iosDarkGray,
                        textColor = iosLightGray,
                        onClick = { viewModel.stopFocusSession() }
                    )
                    
                    // Dummy pause button for aesthetic symmetry (iOS has Cancel and Pause)
                    IosCircularButton(
                        text = "Pause",
                        backgroundColor = iosOrange.copy(alpha = 0.2f),
                        textColor = iosOrange,
                        onClick = { /* Not implemented in backend, aesthetic only */ }
                    )
                }
            }
        } else {
            // Setup Screen
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Focus",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Silence notifications for a set duration.",
                    fontSize = 16.sp,
                    color = iosLightGray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                // iOS dial-like selection aesthetics
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IosCircularButton(
                        text = "25m",
                        backgroundColor = iosDarkGray,
                        textColor = Color.White,
                        onClick = { viewModel.startFocusSession(25) }
                    )
                    IosCircularButton(
                        text = "50m",
                        backgroundColor = iosDarkGray,
                        textColor = Color.White,
                        onClick = { viewModel.startFocusSession(50) }
                    )
                    IosCircularButton(
                        text = "90m",
                        backgroundColor = iosDarkGray,
                        textColor = Color.White,
                        onClick = { viewModel.startFocusSession(90) }
                    )
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Custom time mock display
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(iosDarkGray)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Select a duration to start",
                        color = iosOrange,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun IosCircularButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // iOS border ring (2dp gap from edge)
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = backgroundColor.copy(alpha = 0.5f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
        Text(
            text = text,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

