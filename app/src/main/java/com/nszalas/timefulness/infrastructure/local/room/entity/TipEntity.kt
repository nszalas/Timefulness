package com.nszalas.timefulness.infrastructure.local.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nszalas.timefulness.infrastructure.local.room.Constants.TABLE_TIP
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_TIP)
data class TipEntity (
    @PrimaryKey(autoGenerate = true)
    val tip_id: Int,
    val title: String,
    val description: String,
): Parcelable