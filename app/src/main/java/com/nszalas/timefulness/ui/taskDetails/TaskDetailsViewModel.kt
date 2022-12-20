package com.nszalas.timefulness.ui.taskDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.domain.usecase.CancelTaskReminderUseCase
import com.nszalas.timefulness.domain.usecase.DeleteTaskWithIdUseCase
import com.nszalas.timefulness.domain.usecase.UpdateTaskUseCase
import com.nszalas.timefulness.extensions.EventsChannel
import com.nszalas.timefulness.extensions.asLocalDateTime
import com.nszalas.timefulness.extensions.mutate
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import com.nszalas.timefulness.utils.DateFormatter
import com.nszalas.timefulness.utils.TimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val timeFormatter: TimeFormatter,
    private val dateFormatter: DateFormatter,
    private val updateTask: UpdateTaskUseCase,
    private val deleteTaskWithId: DeleteTaskWithIdUseCase,
    private val cancelTaskReminder: CancelTaskReminderUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TaskDetailsViewState())
    val state: StateFlow<TaskDetailsViewState> = _state.asStateFlow()

    private val _event = EventsChannel<TaskDetailsViewEvent>()
    val event: Flow<TaskDetailsViewEvent> = _event.receiveAsFlow()

    fun onCompletedChecked(completed: Boolean) {
        val task = state.value.taskWithCategory?.task ?: return
        viewModelScope.launch {
            runBlocking(Dispatchers.IO) {
                updateTask(task.copy(completed = completed))
            }
            _state.mutate { copy(completed = completed) }
        }
    }

    fun onEditClicked() {
        _event.trySend(TaskDetailsViewEvent.EditTask)
    }

    fun onDeleteClicked() {
        val task = state.value.taskWithCategory?.task ?: return
        viewModelScope.launch {
            deleteTaskWithId(task.id)
            cancelTaskReminder(task)
            _event.trySend(TaskDetailsViewEvent.DeleteTask)
        }
    }

    fun onTaskEditing(taskWithCategory: TaskWithCategoryUI) {
        with(taskWithCategory) {
            val startDateTime = runBlocking { task.startTimestamp.asLocalDateTime(task.timezoneId) }
            val endDateTime = runBlocking { task.endTimestamp.asLocalDateTime(task.timezoneId) }

            val date = runBlocking { dateFormatter.formatDate(startDateTime.toLocalDate()) }
            val startTime = runBlocking { timeFormatter.formatTime(startDateTime.toLocalTime()) }
            val endTime = runBlocking { timeFormatter.formatTime(endDateTime.toLocalTime()) }

            val category = StringBuilder().apply {
                append(category.emoji)
                append(" ")
                append(category.name)
            }.toString()

            viewModelScope.launch {
                _state.mutate {
                    copy(
                        taskWithCategory = taskWithCategory,
                        title = task.title,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        category = category,
                        completed = task.completed
                    )
                }
            }
        }
    }
}