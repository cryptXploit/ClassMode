package com.classmode.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Define colors directly in Theme.kt to avoid missing references
private val Blue80 = Color(0xFFD0BCFF)
private val Teal80 = Color(0xFFCCC2DC)
private val DarkBackground = Color(0xFF1C1B1F)
private val DarkSurface = Color(0xFF1C1B1F)

private val Blue40 = Color(0xFF6650a4)
private val Teal40 = Color(0xFF625b71)
private val LightBackground = Color(0xFFFFFBFE)
private val LightSurface = Color(0xFFFFFBFE)

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    secondary = Teal80,
    background = DarkBackground,
    surface = DarkSurface
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = Teal40,
    background = LightBackground,
    surface = LightSurface
)

@Composable
fun ClassModeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Android 12+ dynamic colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        // Removed Typography parameter to use MaterialTheme's default typography and avoid kotlin.text.Typography conflicts
        content = content
    )
}
