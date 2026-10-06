package com.cryptxploit.classmode.data.system

import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import com.cryptxploit.classmode.domain.automation.AutomationHealthMonitor
import com.cryptxploit.classmode.domain.model.SoundProfile
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SystemAudioControllerTest {

    private lateinit var context: Context
    private lateinit var audioManager: AudioManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var healthMonitor: AutomationHealthMonitor
    private lateinit var controller: SystemAudioController

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        audioManager = mockk(relaxed = true)
        notificationManager = mockk(relaxed = true)
        healthMonitor = mockk(relaxed = true)

        every { context.getSystemService(Context.AUDIO_SERVICE) } returns audioManager
        every { context.getSystemService(Context.NOTIFICATION_SERVICE) } returns notificationManager

        controller = SystemAudioController(context, healthMonitor)
    }

    @Test
    fun `applyProfile catches SecurityException and degrades gracefully`() {
        every { audioManager.ringerMode = any() } throws SecurityException("Test Exception")

        val result = controller.applyProfile(SoundProfile.NORMAL)
        
        assertFalse(result)
        verify { healthMonitor.logFailure("SecurityException while changing sound profile") }
    }

    @Test
    fun `applyProfile applies profile successfully when permissions exist`() {
        val result = controller.applyProfile(SoundProfile.NORMAL)
        
        assertTrue(result)
        verify { audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL }
    }
}
