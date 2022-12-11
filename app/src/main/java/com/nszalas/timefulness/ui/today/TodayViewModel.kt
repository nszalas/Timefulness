package com.nszalas.timefulness.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.domain.usecase.ObserveAllTasksUseCase
import com.nszalas.timefulness.domain.usecase.UpdateTaskUseCase
import com.nszalas.timefulness.extensions.EventsChannel
import com.nszalas.timefulness.extensions.mutate
import kotlinx.coroutines.launch
import com.nszalas.timefulness.ui.today.TodayViewEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val observeTasks: ObserveAllTasksUseCase,
    private val updateTask: UpdateTaskUseCase,
): ViewModel() {
    private val _state = MutableStateFlow(TodayViewState())
    val state: StateFlow<TodayViewState> = _state.asStateFlow()

    private val _event = EventsChannel<TodayViewEvent>()
    val event: Flow<TodayViewEvent> = _event.receiveAsFlow()

    init {
        observeTaskList()
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
                updateTask(task.copy(completed = checked))
            }
        } catch (e: Exception) {
            // todo try send error event
        }
    }

    private fun observeTaskList() {
        viewModelScope.launch(IO) {
            observeTasks().collect { tasks ->
                _state.mutate { copy(tasks = tasks) }
            }
        }
    }
}