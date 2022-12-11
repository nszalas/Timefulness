package com.nszalas.timefulness.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TaskWithCategoryUI(
    val task: TaskUI,
    val category: CategoryUI,
) : Parcelable