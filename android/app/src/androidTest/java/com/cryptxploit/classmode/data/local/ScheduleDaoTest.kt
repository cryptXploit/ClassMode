package com.cryptxploit.classmode.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cryptxploit.classmode.data.local.dao.ScheduleDao
import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import com.cryptxploit.classmode.domain.model.SessionType
import com.cryptxploit.classmode.domain.model.SoundProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ScheduleDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var scheduleDao: ScheduleDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // Allowed ONLY in testing
            .build()
        scheduleDao = db.scheduleDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeScheduleAndReadInList() = runBlocking {
        val schedule = ScheduleEntity(
            id = 1,
            type = SessionType.CLASS,
            startTimeMins = 600,
            endTimeMins = 690,
            daysOfWeek = 21,
            soundProfile = SoundProfile.SILENT,
            isEnabled = true
        )
        scheduleDao.insertSchedule(schedule)
        
        val activeSchedules = scheduleDao.getActiveSchedules().first()
        assertTrue(activeSchedules.isNotEmpty())
        assertEquals(schedule.type, activeSchedules[0].type)
        assertEquals(SoundProfile.SILENT, activeSchedules[0].soundProfile)
    }
}

