package com.nszalas.timefulness.infrastructure.local.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nszalas.timefulness.infrastructure.local.room.Constants.TABLE_CATEGORY
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = TABLE_CATEGORY)
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    val name: String,
    val colorMain: String,
    val colorText: String,
    val emoji: String,
) : Parcelable