package com.nszalas.timefulness.ui.calendar.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.domain.usecase.GetTasksFromTimeSpanUseCase
import com.nszalas.timefulness.domain.usecase.ObserveAllTasksUseCase
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import com.simplemobiletools.commons.helpers.DAY_SECONDS
import com.simplemobiletools.commons.helpers.WEEK_SECONDS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class WeekFragmentViewModel @Inject constructor(
    private val getTasksFromTimeSpan: GetTasksFromTimeSpanUseCase,
    private val observeAllTasksUseCase: ObserveAllTasksUseCase
) : ViewModel() {
    private val _event = MutableLiveData<WeekFragmentEvent>()
    val event: LiveData<WeekFragmentEvent> = _event

    init {
        observeEventUpdates()
    }

    private fun observeEventUpdates() {
        viewModelScope.launch(IO) {
            observeAllTasksUseCase().collectLatest {
                _event.postValue(
                    WeekFragmentEvent.EventsUpdated
                )
            }
        }
    }

    fun getEvents(weekStartTimestamp: Long) {
        val endTimestamp = weekStartTimestamp + 2 * WEEK_SECONDS
        val startTimestamp = weekStartTimestamp - DAY_SECONDS

        viewModelScope.launch(IO) {
            getTasksFromTimeSpan(startTimestamp, endTimestamp).let { tasks ->
                _event.postValue(
                    WeekFragmentEvent.EventsLoaded(ArrayList(tasks))
                )
            }
        }
    }
}

sealed class WeekFragmentEvent {
    data class EventsLoaded(val tasks: ArrayList<TaskWithCategoryUI>) : WeekFragmentEvent()
    object EventsUpdated : WeekFragmentEvent()
}