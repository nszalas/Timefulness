package com.nszalas.timefulness.infrastructure.local.room.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_CATEGORY_ID
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_TASK_CATEGORY_ID
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskWithCategoryEntity (
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = COLUMN_TASK_CATEGORY_ID,
        entityColumn = COLUMN_CATEGORY_ID
    )
    val category: CategoryEntity
) : Parcelable