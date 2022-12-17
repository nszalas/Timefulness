package com.nszalas.timefulness.ui.today

import com.nszalas.timefulness.ui.model.TaskWithCategoryUI

data class TodayViewState(
    val tasks: List<TaskWithCategoryUI> = emptyList(),
    val currentDate: String? = null
)

sealed class TodayViewEvent {
    data class TaskClicked(val id: Int) : TodayViewEvent()
    data class TaskChecked(val id: Int, val checked: Boolean) : TodayViewEvent()
    object AddTaskClicked : TodayViewEvent()
}