package com.nszalas.timefulness.ui.calendar.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nszalas.timefulness.ui.calendar.room.models.Event
import com.nszalas.timefulness.ui.calendar.utils.TYPE_EVENT
import com.nszalas.timefulness.ui.calendar.utils.TYPE_TASK
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Query("SELECT * FROM events")
    fun observeAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE id = :id AND (type = $TYPE_EVENT OR type = $TYPE_TASK)")
    fun getEventOrTaskWithId(id: Long): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(event: Event): Long
}
