package com.nszalas.timefulness.ui.calendar.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nszalas.timefulness.R
import com.nszalas.timefulness.ui.calendar.room.models.Event
import com.nszalas.timefulness.ui.calendar.room.models.EventType
import com.nszalas.timefulness.ui.calendar.room.dao.EventTypesDao
import com.nszalas.timefulness.ui.calendar.room.dao.EventsDao
import com.nszalas.timefulness.ui.calendar.utils.REGULAR_EVENT_TYPE_ID
import com.simplemobiletools.commons.extensions.getProperPrimaryColor
import java.util.concurrent.Executors

// todo remove migrations of db
@Database(entities = [Event::class, EventType::class], version = 10)
abstract class EventsDatabase : RoomDatabase() {

    abstract fun EventsDao(): EventsDao

    abstract fun EventTypesDao(): EventTypesDao

    companion object {
        private var db: EventsDatabase? = null

        fun getInstance(context: Context): EventsDatabase {
            if (db == null) {
                synchronized(EventsDatabase::class) {
                    if (db == null) {
                        db = Room.databaseBuilder(
                            context.applicationContext,
                            EventsDatabase::class.java,
                            "mydatabase"
                        )
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    insertRegularEventType(context)
                                }
                            })
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .addMigrations(MIGRATION_3_4)
                            .addMigrations(MIGRATION_4_5)
                            .addMigrations(MIGRATION_5_6)
                            .addMigrations(MIGRATION_6_7)
                            .addMigrations(MIGRATION_7_8)
                            .addMigrations(MIGRATION_8_9)
                            .addMigrations(MIGRATION_9_10)
                            .build()
                        db!!.openHelper.setWriteAheadLoggingEnabled(true)
                    }
                }
            }
            return db!!
        }

        private fun insertRegularEventType(context: Context) {
            Executors.newSingleThreadScheduledExecutor().execute {
                val regularEvent = context.resources.getString(R.string.regular_event)
                val eventType =
                    EventType(REGULAR_EVENT_TYPE_ID, regularEvent, context.getProperPrimaryColor())
                db!!.EventTypesDao().insertOrUpdate(eventType)
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("ALTER TABLE events ADD COLUMN reminder_1_type INTEGER NOT NULL DEFAULT 0")
                    execSQL("ALTER TABLE events ADD COLUMN reminder_2_type INTEGER NOT NULL DEFAULT 0")
                    execSQL("ALTER TABLE events ADD COLUMN reminder_3_type INTEGER NOT NULL DEFAULT 0")
                    execSQL("ALTER TABLE events ADD COLUMN attendees TEXT NOT NULL DEFAULT ''")
                }
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("ALTER TABLE events ADD COLUMN time_zone TEXT NOT NULL DEFAULT ''")
                }
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("ALTER TABLE events ADD COLUMN availability INTEGER NOT NULL DEFAULT 0")
                }
            }
        }

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("CREATE TABLE IF NOT EXISTS `widgets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `widget_id` INTEGER NOT NULL, `period` INTEGER NOT NULL)")
                    execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_widgets_widget_id` ON `widgets` (`widget_id`)")
                }
            }
        }

        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("ALTER TABLE events ADD COLUMN type INTEGER NOT NULL DEFAULT 0")
                }
            }
        }

        private val MIGRATION_6_7 = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("CREATE TABLE IF NOT EXISTS `tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `task_id` INTEGER NOT NULL, `start_ts` INTEGER NOT NULL, `flags` INTEGER NOT NULL)")
                    execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_tasks_id_task_id` ON `tasks` (`id`, `task_id`)")
                }
            }
        }

        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("ALTER TABLE event_types ADD COLUMN type INTEGER NOT NULL DEFAULT 0")
                }
            }
        }

        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("DROP TABLE `widgets`")
                }
            }
        }

        private val MIGRATION_9_10 = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase){
                // events table migration
                database.apply {
                    execSQL("""
                        CREATE TABLE newEvents (
                            id INTEGER PRIMARY KEY NOT NULL,
                            start_ts INTEGER NOT NULL,
                            end_ts INTEGER NOT NULL,
                            title TEXT NOT NULL,
                            repeat_interval INTEGER NOT NULL,
                            repeat_rule INTEGER NOT NULL,
                            repeat_limit INTEGER NOT NULL,
                            time_zone TEXT NOT NULL,
                            flags INTEGER NOT NULL,
                            event_type INTEGER NOT NULL,
                            last_updated INTEGER NOT NULL,
                            color INTEGER NOT NULL,
                            type INTEGER NOT NULL
                        )
                    """.trimIndent())
                    execSQL("""
                        INSERT INTO newEvents (
                        id, start_ts, end_ts, title, repeat_interval, repeat_rule,
                        repeat_limit, time_zone, flags, event_type, last_updated, color, type
                        ) SELECT id, start_ts, end_ts, title, repeat_interval, repeat_rule,
                        repeat_limit, time_zone, flags, event_type, last_updated, color, type FROM events
                    """.trimIndent())
                    execSQL("DROP TABLE events")
                    execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_events_id ON newEvents(id)")
                    execSQL("ALTER TABLE newEvents RENAME TO  events")
                }
                // event types table migration
                database.apply {
                    execSQL("""
                        CREATE TABLE newEventTypes (
                            id INTEGER PRIMARY KEY NOT NULL,
                            title TEXT NOT NULL,
                            color INTEGER NOT NULL,
                            type INTEGER NOT NULL
                        )
                    """.trimIndent())
                    execSQL("""
                        INSERT INTO newEventTypes (
                        id, title, color, type
                        ) SELECT id, title, color, type FROM event_types
                    """.trimIndent())
                    execSQL("DROP TABLE event_types")
                    execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_event_types_id ON newEventTypes(id)")
                    execSQL("ALTER TABLE newEventTypes RENAME TO event_types")
                }
            }
        }
    }
}
