package com.cryptxploit.classmode.data.receiver

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlinx.coroutines.test.runTest

class SystemEventReceiverTest {
    @Test
    fun `receiver ignores unrelated intents gracefully`() {
        val context = mockk<Context>(relaxed = true)
        val intent = Intent("com.unrelated.action")
        val receiver = SystemEventReceiver()
        
        // This should not crash
        receiver.onReceive(context, intent)
    }
}
