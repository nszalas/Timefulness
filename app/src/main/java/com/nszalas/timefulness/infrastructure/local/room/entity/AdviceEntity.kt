package com.nszalas.timefulness.infrastructure.local.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nszalas.timefulness.infrastructure.local.room.Constants.TABLE_ADVICE
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_ADVICE)
data class AdviceEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
): Parcelable