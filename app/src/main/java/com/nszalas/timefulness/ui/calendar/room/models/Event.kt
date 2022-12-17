package com.nszalas.timefulness.ui.calendar.room.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nszalas.timefulness.ui.calendar.utils.FLAG_TASK_COMPLETED
import com.nszalas.timefulness.ui.calendar.utils.REGULAR_EVENT_TYPE_ID
import com.nszalas.timefulness.ui.calendar.utils.TYPE_EVENT
import com.nszalas.timefulness.ui.calendar.utils.TYPE_TASK
import java.io.Serializable

@Entity(tableName = "events", indices = [(Index(value = ["id"], unique = true))])
data class Event(
    @PrimaryKey(autoGenerate = true) var id: Long = -1,
    @ColumnInfo(name = "start_ts") var startTS: Long = 0L,
    @ColumnInfo(name = "end_ts") var endTS: Long = 0L,
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "repeat_interval") var repeatInterval: Int = 0,
    @ColumnInfo(name = "repeat_rule") var repeatRule: Int = 0,
    @ColumnInfo(name = "repeat_limit") var repeatLimit: Long = 0L,
    @ColumnInfo(name = "time_zone") var timeZone: String = "",
    @ColumnInfo(name = "flags") var flags: Int = 0,
    @ColumnInfo(name = "event_type") var eventType: Long = REGULAR_EVENT_TYPE_ID,
    @ColumnInfo(name = "last_updated") var lastUpdated: Long = 0L,
    @ColumnInfo(name = "color") var color: Int = 0,
    @ColumnInfo(name = "type") var type: Int = TYPE_EVENT
) : Serializable {

    companion object {
        private const val serialVersionUID = -32456795132345616L
    }

    fun isTask() = type == TYPE_TASK
    fun isTaskCompleted() = isTask() && flags and FLAG_TASK_COMPLETED != 0
}
