package com.nszalas.timefulness.ui.addTask

data class AddTaskViewState(
    val categories: List<String> = listOf("Home", "Family", "Work")
)

sealed class AddTaskViewEvent {
    object TaskAdded: AddTaskViewEvent()
}