package com.cryptxploit.classmode.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import com.cryptxploit.classmode.data.system.SystemHapticController

val LocalHaptic = staticCompositionLocalOf<SystemHapticController> {
    error("No SystemHapticController provided. Ensure it is provided in MainActivity.")
}

