package com.nszalas.timefulness.ui.calendar.room.dao

import androidx.room.*
import com.nszalas.timefulness.ui.calendar.room.models.EventType

@Dao
interface EventTypesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(eventType: EventType): Long
}
