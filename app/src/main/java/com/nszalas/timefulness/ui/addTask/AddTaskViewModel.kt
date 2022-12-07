package com.nszalas.timefulness.ui.addTask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.extensions.EventsChannel
import com.nszalas.timefulness.extensions.sendIfActive
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddTaskViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddTaskViewState())
    val state: StateFlow<AddTaskViewState> = _state.asStateFlow()

    private val _event = EventsChannel<AddTaskViewEvent>()
    val event: Flow<AddTaskViewEvent> = _event.receiveAsFlow()

    fun addTask() {
        viewModelScope.launch(IO) {
            _event.sendIfActive(AddTaskViewEvent.TaskAdded)
        }
    }
}