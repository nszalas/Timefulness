package com.nszalas.timefulness.ui.calendar.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nszalas.timefulness.ui.calendar.utils.OTHER_EVENT

@Entity(tableName = "event_types", indices = [(Index(value = ["id"], unique = true))])
data class EventType(
    @PrimaryKey(autoGenerate = true) var id: Long = -1,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "color") var color: Int,
    @ColumnInfo(name = "type") var type: Int = OTHER_EVENT
)
