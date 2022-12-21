package com.nszalas.timefulness.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.domain.usecase.ObserveTasksForDateUseCase
import com.nszalas.timefulness.domain.usecase.UpdateTaskCompletionUseCase
import com.nszalas.timefulness.extensions.EventsChannel
import com.nszalas.timefulness.extensions.mutate
import kotlinx.coroutines.launch
import com.nszalas.timefulness.ui.today.TodayViewEvent.*
import com.nszalas.timefulness.utils.DateTimeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val updateTask: UpdateTaskCompletionUseCase,
    private val observeTasksForDate: ObserveTasksForDateUseCase,
    private val dateTimeProvider: DateTimeProvider,
): ViewModel() {
    private val _state = MutableStateFlow(TodayViewState())
    val state: StateFlow<TodayViewState> = _state.asStateFlow()

    private val _event = EventsChannel<TodayViewEvent>()
    val event: Flow<TodayViewEvent> = _event.receiveAsFlow()

    fun onRefresh() {
        updateCurrentDate()
        observeTaskList()
    }

    private fun updateCurrentDate() {
        val date = dateTimeProvider.currentDate().format(DateTimeFormatter.ofPattern(DATE_PATTERN))
        viewModelScope.launch {
            _state.mutate {
                copy(currentDate = date)
            }
        }
    }

    fun onAddTaskClicked() {
        _event.trySend(AddTaskClicked)
    }

    fun onTaskClicked(id: Int) {
        _event.trySend(TaskClicked(id))
    }

    fun onTaskChecked(position: Int, checked: Boolean) {
        try {
            val task = state.value.tasks[position].task
            viewModelScope.launch(IO) {
                updateTask(task, checked)
            }
        } catch (e: Exception) {
            // todo try send error event
        }
    }

    private fun observeTaskList() {
        viewModelScope.launch(IO) {
            observeTasksForDate(dateTimeProvider.currentDate()).collect { tasks ->
                _state.mutate { copy(tasks = tasks) }
            }
        }
    }

    companion object {
        private const val DATE_PATTERN = "dd MMMM yyyy"
    }
}