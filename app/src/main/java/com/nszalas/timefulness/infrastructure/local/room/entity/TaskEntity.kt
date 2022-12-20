package com.nszalas.timefulness.infrastructure.local.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_TASK_ID
import com.nszalas.timefulness.infrastructure.local.room.Constants.TABLE_TASK
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_TASK, indices = [(Index(value = [COLUMN_TASK_ID], unique = true))])
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val title: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
    val timezoneId: String,
    val categoryId: Int,
    val repeat: Boolean = false,
    val completed: Boolean = false,
) : Parcelable