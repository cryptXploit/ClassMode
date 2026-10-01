package com.cryptxploit.classmode.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cryptxploit.classmode.data.local.dao.ScheduleDao
import com.cryptxploit.classmode.data.local.dao.AutomationEventDao
import com.cryptxploit.classmode.data.local.dao.TriggerStateDao
import com.cryptxploit.classmode.data.local.entity.GeofenceEntity
import com.cryptxploit.classmode.data.local.entity.ScheduleEntity
import com.cryptxploit.classmode.data.local.entity.AutomationEventEntity
import com.cryptxploit.classmode.data.local.entity.TriggerStateEntity
import com.cryptxploit.classmode.data.local.entity.AlarmEntity
import com.cryptxploit.classmode.data.local.dao.AlarmDao

@Database(
    entities = [ScheduleEntity::class, GeofenceEntity::class, AutomationEventEntity::class, TriggerStateEntity::class, AlarmEntity::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun geofenceDao(): com.cryptxploit.classmode.data.local.dao.GeofenceDao
    abstract fun automationEventDao(): AutomationEventDao
    abstract fun triggerStateDao(): TriggerStateDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE schedules ADD COLUMN title TEXT NOT NULL DEFAULT ''")
                database.execSQL("ALTER TABLE schedules ADD COLUMN soundProfile TEXT NOT NULL DEFAULT 'VIBRATE'")
                database.execSQL("ALTER TABLE schedules ADD COLUMN condition TEXT NOT NULL DEFAULT 'TIME_ONLY'")
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `geofences` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ruleId` INTEGER NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `radiusMeters` REAL NOT NULL)"
                )
            }
        }
        
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `automation_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `ruleId` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, `result` TEXT NOT NULL, `details` TEXT NOT NULL)"
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `trigger_states` (`ruleId` INTEGER PRIMARY KEY NOT NULL, `isTimeActive` INTEGER NOT NULL, `isLocationActive` INTEGER NOT NULL, `lastUpdated` INTEGER NOT NULL)"
                )
            }
        }

                                val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `alarms` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timeMins` INTEGER NOT NULL, `daysOfWeek` INTEGER NOT NULL, `isEnabled` INTEGER NOT NULL, `isVibrationEnabled` INTEGER NOT NULL, `snoozeMins` INTEGER NOT NULL, `label` TEXT NOT NULL)")
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                runCatching { database.execSQL("DROP TABLE IF EXISTS `\u0007larms`") }
                runCatching { database.execSQL("DROP TABLE IF EXISTS `larms`") }
                runCatching { database.execSQL("DROP TABLE IF EXISTS `\u000alarms`") }
                runCatching { database.execSQL("DROP TABLE IF EXISTS `alarms`") }
                
                database.execSQL("CREATE TABLE IF NOT EXISTS `alarms` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `timeMins` INTEGER NOT NULL, `daysOfWeek` INTEGER NOT NULL, `isEnabled` INTEGER NOT NULL, `isVibrationEnabled` INTEGER NOT NULL, `snoozeMins` INTEGER NOT NULL, `label` TEXT NOT NULL)")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "classmode_database"
                )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


