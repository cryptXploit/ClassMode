package com.cryptxploit.classmode.presentation.ads

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

/**
 * Privacy-compliant AdsManager.
 * Ensures ads do not block critical paths and do not display during active focus sessions.
 */
object AdsManager {
    // Official Google Test IDs. Must be replaced via BuildConfig in production.
    const val TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"

    fun initialize(context: Context) {
        // Initialize the SDK asynchronously on a background thread
        MobileAds.initialize(context) { initializationStatus ->
            // Logging initialized status (without blocking main thread)
        }
    }
}

@Composable
fun AdaptiveBannerAd(
    modifier: Modifier = Modifier,
    isAutomationActive: Boolean
) {
    // Monetization Rule: No ads during active class automation.
    if (isAutomationActive) {
        return
    }

    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = AdsManager.TEST_BANNER_ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

