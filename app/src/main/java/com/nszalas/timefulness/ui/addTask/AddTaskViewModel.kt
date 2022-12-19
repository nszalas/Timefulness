package com.nszalas.timefulness.ui.addTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.domain.usecase.*
import com.nszalas.timefulness.extensions.*
import com.nszalas.timefulness.ui.model.CategoryUI
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import com.nszalas.timefulness.utils.DateFormatter
import com.nszalas.timefulness.utils.DateTimeProvider
import com.nszalas.timefulness.utils.TaskNameValidator
import com.nszalas.timefulness.utils.TimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val dateTimeProvider: DateTimeProvider,
    private val dateFormatter: DateFormatter,
    private val timeFormatter: TimeFormatter,
    private val taskNameValidator: TaskNameValidator,
    private val getCategories: GetCategoriesUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val insertTask: InsertTaskUseCase,
    private val deleteTaskWithId: DeleteTaskWithIdUseCase,
    private val setTaskReminderUseCase: SetTaskReminderUseCase,
    private val cancelTaskReminderUseCase: CancelTaskReminderUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AddTaskViewState())
    val state: StateFlow<AddTaskViewState> = _state.asStateFlow()

    private val _event = EventsChannel<AddTaskViewEvent>()
    val event: Flow<AddTaskViewEvent> = _event.receiveAsFlow()

    init {
        val categories = runBlocking(IO) { getCategories() }

        viewModelScope.launch {
            val startTime = dateTimeProvider.currentTime()
            val endTime = startTime.plusHours(1)

            _state.mutate {
                copy(
                    date = dateFormatter.formatDate(dateTimeProvider.currentDate()),
                    startTime = timeFormatter.formatTime(startTime),
                    endTime = timeFormatter.formatTime(endTime),
                    categories = categories
                )
            }
            validateEndTime()
            validateStartTime()
        }
    }

    fun onEventNameEntered(name: String?) {
        viewModelScope.launch {
            val error = taskNameValidator.isValid(name)
            val buttonEnabled = error == null
            _state.mutate {
                copy(
                    taskTitle = name,
                    taskTitleError = error,
                    addButtonEnabled = buttonEnabled
                )
            }
        }
    }

    fun onCategoryPicked(category: CategoryUI) {
        viewModelScope.launch {
            _state.mutate {
                copy(
                    categoryId = category.id
                )
            }
        }
    }

    // time events

    fun onStartTimeClicked() {
        viewModelScope.launch {
            val time = state.value.startTime ?: return@launch
            _event.trySend(
                AddTaskViewEvent.OpenTimePicker(
                    timeFormatter.parseTime(time),
                    TaskTimeType.START
                )
            )
        }
    }

    fun onEndTimeClicked() {
        viewModelScope.launch {
            val time = state.value.endTime ?: return@launch
            _event.trySend(
                AddTaskViewEvent.OpenTimePicker(
                    timeFormatter.parseTime(time),
                    TaskTimeType.END
                )
            )
        }
    }

    fun onTimePicked(hours: Int, minutes: Int, taskTimeType: TaskTimeType) {
        viewModelScope.launch {
            _state.mutate {
                when (taskTimeType) {
                    TaskTimeType.START -> copy(
                        startTime = timeFormatter.parseTimeToString(hours, minutes)
                    )
                    TaskTimeType.END -> copy(
                        endTime = timeFormatter.parseTimeToString(hours, minutes)
                    )
                }
            }
            validateTimePicked(taskTimeType)
        }
    }

    private fun validateTimePicked(taskTimeType: TaskTimeType = TaskTimeType.END) {
        if (taskTimeType == TaskTimeType.START) {
            validateEndTime()
        } else {
            validateStartTime()
        }
    }

    private fun validateStartTime() {
        viewModelScope.launch {
            with(state.value) {
                val startTime = timeFormatter.parseTime(startTime)
                val endTime = timeFormatter.parseTime(endTime)
                when {
                    startTime >= endTime && endTime.hour == 0 -> {
                        _state.mutate {
                            copy(
                                startTime = timeFormatter.formatTime(endTime.withMinute(0))
                            )
                        }
                    }
                    startTime >= endTime && endTime.hour > 0 -> {
                        _state.mutate {
                            copy(startTime = timeFormatter.formatTime(endTime.minusHours(1)))
                        }
                    }
                }
            }
        }
    }

    private fun validateEndTime() {
        viewModelScope.launch {
            with(state.value) {
                val startTime = timeFormatter.parseTime(startTime)
                val endTime = timeFormatter.parseTime(endTime)
                when {
                    startTime >= endTime && startTime.hour == 23 -> {
                        _state.mutate {
                            copy(
                                endTime = timeFormatter.formatTime(startTime.withMinute(59))
                            )
                        }
                    }
                    startTime >= endTime && startTime.hour < 23 -> {
                        _state.mutate {
                            copy(endTime = timeFormatter.formatTime(startTime.plusHours(1)))
                        }
                    }
                }
            }
        }
    }

    // date events

    fun onDateClicked() {
        viewModelScope.launch {
            val date = state.value.date ?: return@launch
            _event.trySend(
                AddTaskViewEvent.OpenDatePicker(
                    dateFormatter.parseDate(date)
                )
            )
        }
    }

    fun onDatePicked(year: Int, monthOfYear: Int, dayOfMonth: Int) {
        viewModelScope.launch {
            _state.mutate {
                copy(
                    date = dateFormatter.parseDateToString(year, monthOfYear, dayOfMonth)
                )
            }
        }
    }

    // task events

    fun onTaskEditing(taskWithCategory: TaskWithCategoryUI) {
        with(taskWithCategory) {
            val startDateTime = runBlocking { task.startTimestamp.asLocalDateTime(task.timezoneId) }
            val endDateTime = runBlocking { task.endTimestamp.asLocalDateTime(task.timezoneId) }

            val date = runBlocking { dateFormatter.formatDate(startDateTime.toLocalDate()) }
            val startTime = runBlocking { timeFormatter.formatTime(startDateTime.toLocalTime()) }
            val endTime = runBlocking { timeFormatter.formatTime(endDateTime.toLocalTime()) }

            viewModelScope.launch {
                _state.mutate {
                    copy(
                        taskTitle = task.title,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        categoryId = category.id,
                        isEditing = true,
                        editingTaskId = task.id,
                        editingTaskCompleted = task.completed
                    )
                }
            }
        }
    }

    fun onAddClicked() { addTask() }

    fun onDeleteClicked() { deleteTask() }

    private fun addTask() {
        val task = getTaskFromState() ?: return

        runBlocking(IO) { insertTask(task) }

        when(state.value.isEditing) {
            true -> updateTaskReminder(task)
            false -> setTaskReminder(task)
        }

        viewModelScope.launch {
            _event.sendIfActive(AddTaskViewEvent.NavigateBack)
        }
    }

    private fun deleteTask() {
        val task = getTaskFromState() ?: return

        if(state.value.isEditing) {
            val taskId = state.value.editingTaskId

            runBlocking(IO) { deleteTaskWithId(taskId) }
            cancelTaskReminder(task)

            viewModelScope.launch {
                _event.trySend(AddTaskViewEvent.NavigateBack)
            }
        }
    }

    private fun getTaskFromState(): Task? {
        val user = getCurrentUser()
        user ?: return null

        val localDate = dateFormatter.parseDate(state.value.date)
        val localStartTime = timeFormatter.parseTime(state.value.startTime)
        val localEndTime = timeFormatter.parseTime(state.value.endTime)

        val taskId = state.value.editingTaskId
        val taskCompleted = state.value.editingTaskCompleted

        return Task(
            id = taskId,
            userId = user.id,
            title = state.value.taskTitle,
            categoryId = state.value.categoryId,
            startTimestamp = LocalDateTime.of(localDate, localStartTime).asTimestamp(),
            endTimestamp = LocalDateTime.of(localDate, localEndTime).asTimestamp(),
            timezoneId = ZoneId.systemDefault().id,
            completed = taskCompleted,
        )
    }

    // reminder notifications

    private fun updateTaskReminder(task: Task) {
        task.run {
            let(::cancelTaskReminder)
            let(::setTaskReminder)
        }
    }

    private fun setTaskReminder(task: Task) {
        setTaskReminderUseCase(task)
    }

    private fun cancelTaskReminder(task: Task) {
        cancelTaskReminderUseCase(task)
    }
}