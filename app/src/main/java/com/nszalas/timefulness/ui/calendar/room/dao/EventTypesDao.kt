package com.nszalas.timefulness.ui.calendar.room.dao

import androidx.room.*
import com.nszalas.timefulness.ui.calendar.room.models.EventType

@Dao
interface EventTypesDao {
    @Query("SELECT * FROM event_types ORDER BY title ASC")
    fun getEventTypes(): List<EventType>

    @Query("SELECT * FROM event_types WHERE id = :id")
    fun getEventTypeWithId(id: Long): EventType?

    @Query("SELECT id FROM event_types WHERE title = :title COLLATE NOCASE")
    fun getEventTypeIdWithTitle(title: String): Long?

    @Query("SELECT id FROM event_types WHERE type = :classId")
    fun getEventTypeIdWithClass(classId: Int): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(eventType: EventType): Long

    @Delete
    fun deleteEventTypes(eventTypes: List<EventType>)
}
