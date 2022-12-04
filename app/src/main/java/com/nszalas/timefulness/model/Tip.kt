package com.nszalas.timefulness.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tip_table")
data class Tip (
    @PrimaryKey(autoGenerate = true)
    val tip_id: Int,
    val title: String,
    val description: String,
): Parcelable