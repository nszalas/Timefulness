package com.nszalas.timefulness.ui.addTask

import com.nszalas.timefulness.ui.model.CategoryUI
import java.time.LocalDate
import java.time.LocalTime

data class AddTaskViewState(
    val categories: List<CategoryUI> = emptyList(),
    val date: String? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val taskTitle: String? = null,
    val categoryId: Int? = null,
    val taskTitleError: Int? = null,
    val addButtonEnabled: Boolean = false,
)

sealed class AddTaskViewEvent {
    object TaskAdded: AddTaskViewEvent()
    data class OpenTimePicker(val localTime: LocalTime, val taskTimeType: TaskTimeType): AddTaskViewEvent()
    data class OpenDatePicker(val localDate: LocalDate): AddTaskViewEvent()
}

enum class TaskTimeType {
    START, END
}