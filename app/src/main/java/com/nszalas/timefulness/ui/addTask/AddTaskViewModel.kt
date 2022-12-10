package com.nszalas.timefulness.ui.addTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.nszalas.timefulness.extensions.EventsChannel
import com.nszalas.timefulness.extensions.mutate
import com.nszalas.timefulness.extensions.sendIfActive
import com.nszalas.timefulness.model.AppDatabase
import com.nszalas.timefulness.model.Task
import com.nszalas.timefulness.utils.DateFormatter
import com.nszalas.timefulness.utils.DateTimeProvider
import com.nszalas.timefulness.utils.TaskNameValidator
import com.nszalas.timefulness.utils.TimeFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dateTimeProvider: DateTimeProvider,
    private val dateFormatter: DateFormatter,
    private val timeFormatter: TimeFormatter,
    private val taskNameValidator: TaskNameValidator,
    private val database: AppDatabase,
) : ViewModel() {
    private val _state = MutableStateFlow(AddTaskViewState())
    val state: StateFlow<AddTaskViewState> = _state.asStateFlow()

    private val _event = EventsChannel<AddTaskViewEvent>()
    val event: Flow<AddTaskViewEvent> = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            val startTime = dateTimeProvider.currentTime()
            val endTime = startTime.plusHours(1)

            _state.mutate {
                copy(
                    date = dateFormatter.formatDate(dateTimeProvider.currentDate()),
                    startTime = timeFormatter.formatTime(startTime),
                    endTime = timeFormatter.formatTime(endTime)
                )
            }
            validateTimePicked()
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
                                startTime = timeFormatter.formatTime(startTime.withMinute(0))
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

    fun onAddButtonClicked() {
        addTask()
    }

    private fun addTask() {
        val user = firebaseAuth.currentUser
        user ?: return

        val task = Task(
            task_id = 0,
            user_id = user.uid,
            description = state.value.taskTitle!!,
            date = state.value.date!!,
            time = state.value.startTime!!,
            category = "kategoria",
            repeat = false,
            done = false
        )
        runBlocking(IO) {
            database.taskDao().insert(task)
            _event.sendIfActive(AddTaskViewEvent.TaskAdded)
        }
    }
}