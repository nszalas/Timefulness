package com.nszalas.timefulness.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.extensions.EventsChannel
import com.nszalas.timefulness.ui.calendar.room.EventsDatabase
import com.nszalas.timefulness.ui.calendar.room.models.Event
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor() : ViewModel() {
    private val _event = EventsChannel<CalendarViewEvent>()
    val event: Flow<CalendarViewEvent> = _event.receiveAsFlow()

    fun onAddTaskClicked() {
        viewModelScope.launch {
            _event.trySend(CalendarViewEvent.NavigateToAddTask)
        }
    }

    fun onTaskClicked(task: TaskWithCategoryUI) {
        viewModelScope.launch {
            _event.trySend(CalendarViewEvent.NavigateEditTask(task))
        }
    }
}

sealed class CalendarViewEvent {
    object NavigateToAddTask : CalendarViewEvent()
    data class NavigateEditTask(val task: TaskWithCategoryUI) : CalendarViewEvent()
}