package com.nszalas.timefulness.ui.calendar.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nszalas.timefulness.ui.calendar.room.models.Event
import com.nszalas.timefulness.ui.calendar.utils.REGULAR_EVENT_TYPE_ID
import com.nszalas.timefulness.ui.calendar.utils.TYPE_EVENT
import com.nszalas.timefulness.ui.calendar.utils.TYPE_TASK
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Query("SELECT * FROM events")
    fun observeAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events")
    fun getAllEvents(): List<Event>

    @Query("SELECT * FROM events WHERE id = :id AND type = $TYPE_EVENT")
    fun getEventWithId(id: Long): Event?

    @Query("SELECT * FROM events WHERE id = :id AND type = $TYPE_TASK")
    fun getTaskWithId(id: Long): Event?

    @Query("SELECT * FROM events WHERE id = :id AND (type = $TYPE_EVENT OR type = $TYPE_TASK)")
    fun getEventOrTaskWithId(id: Long): Event?

    @Query("SELECT * FROM events WHERE start_ts <= :toTS AND end_ts >= :fromTS AND (type = $TYPE_EVENT OR type = $TYPE_TASK)")
    fun getEventsOrTasksFromTo(toTS: Long, fromTS: Long): List<Event>

    @Query("SELECT * FROM events WHERE start_ts <= :toTS AND end_ts >= :fromTS AND start_ts != 0 AND repeat_interval = 0 AND event_type IN (:eventTypeIds) AND (type = $TYPE_EVENT OR type = $TYPE_TASK)")
    fun getOneTimeEventsFromToWithTypes(
        toTS: Long,
        fromTS: Long,
        eventTypeIds: List<Long>
    ): List<Event>

    @Query("SELECT * FROM events WHERE start_ts <= :toTS AND (type = $TYPE_EVENT OR type = $TYPE_TASK)")
    fun getEventsOrTasksWithTypes(toTS: Long): List<Event>

    @Query("SELECT * FROM events WHERE id = :id AND start_ts <= :toTS AND repeat_interval != 0 AND (type = $TYPE_EVENT OR type = $TYPE_TASK)")
    fun getRepeatableEventsOrTasksWithId(id: Long, toTS: Long): List<Event>

    @Query("SELECT * FROM events WHERE start_ts <= :toTS AND start_ts != 0 AND repeat_interval != 0 AND event_type IN (:eventTypeIds) AND (type = $TYPE_EVENT OR type = $TYPE_TASK)")
    fun getRepeatableEventsOrTasksWithTypes(toTS: Long, eventTypeIds: List<Long>): List<Event>

    @Query("SELECT * FROM events WHERE repeat_interval != 0 AND (repeat_limit == 0 OR repeat_limit > :currTS) AND event_type IN (:eventTypeIds) AND type = $TYPE_EVENT")
    fun getRepeatableFutureEventsWithTypes(currTS: Long, eventTypeIds: List<Long>): List<Event>

    @Query("SELECT * FROM events WHERE id IN (:ids) AND type = $TYPE_EVENT")
    fun getEventsWithIds(ids: List<Long>): List<Event>

    @Query("SELECT id FROM events")
    fun getEventIds(): List<Long>

    @Query("SELECT id FROM events WHERE event_type = :eventTypeId AND type = $TYPE_EVENT")
    fun getEventIdsByEventType(eventTypeId: Long): List<Long>

    @Query("SELECT id FROM events WHERE event_type IN (:eventTypeIds) AND type = $TYPE_EVENT")
    fun getEventIdsByEventType(eventTypeIds: List<Long>): List<Long>

    @Query("UPDATE events SET event_type = $REGULAR_EVENT_TYPE_ID WHERE event_type = :eventTypeId AND type = $TYPE_EVENT")
    fun resetEventsWithType(eventTypeId: Long)

    @Deprecated("Use Context.updateTaskCompletion() instead unless you know what you are doing.")
    @Query("UPDATE events SET flags = :newFlags WHERE id = :id")
    fun updateTaskCompletion(id: Long, newFlags: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(event: Event): Long

    @Query("DELETE FROM events WHERE id IN (:ids)")
    fun deleteEvents(ids: List<Long>)

    @Query("DELETE FROM events WHERE id = (:id)")
    fun deleteEvent(id: Long)
}
