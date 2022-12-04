package com.nszalas.timefulness.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val task_id: Int,
    val user_id: String,
    val description: String,
    val date: String,
    val time: String,
    val category: String,
    val repeat: Boolean,
    val done: Boolean,
    ): Parcelable