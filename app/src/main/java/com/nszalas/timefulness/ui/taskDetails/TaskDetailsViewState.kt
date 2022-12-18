package com.nszalas.timefulness.ui.taskDetails

import com.nszalas.timefulness.ui.model.TaskWithCategoryUI

data class TaskDetailsViewState(
    val taskWithCategory: TaskWithCategoryUI? = null,
    val title: String? = null,
    val date: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val category: String? = null,
    val completed: Boolean = false
)

sealed class TaskDetailsViewEvent {
    object DeleteTask : TaskDetailsViewEvent()
    object EditTask : TaskDetailsViewEvent()
}