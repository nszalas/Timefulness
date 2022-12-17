package com.nszalas.timefulness.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.ui.calendar.room.EventsDatabase
import com.nszalas.timefulness.ui.calendar.room.models.Event
import com.nszalas.timefulness.ui.calendar.room.models.EventType
import com.simplemobiletools.commons.helpers.DAY_SECONDS
import com.simplemobiletools.commons.helpers.WEEK_SECONDS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class WeekFragmentViewModel @Inject constructor(
    private val database: EventsDatabase
) : ViewModel() {
    private val _event = MutableLiveData<WeekFragmentEvent>()
    val event: LiveData<WeekFragmentEvent> = _event

    init {
        observeEventUpdates()
    }

    private fun observeEventUpdates() {
        viewModelScope.launch(IO) {
            database.EventsDao().observeAllEvents().collect {
                _event.postValue(
                    WeekFragmentEvent.EventsUpdated
                )
            }
        }
    }

    fun getEvents(weekStartTimestamp: Long) {
        val endTimestamp = weekStartTimestamp + 2 * WEEK_SECONDS
        val startTimestamp = weekStartTimestamp - DAY_SECONDS

//        val displayEventTypes = context.config.displayEventTypes
        viewModelScope.launch(IO) {
            _event.postValue(
                WeekFragmentEvent.EventsLoaded(
                    database.EventsDao().getEventsOrTasksFromTo(
                        endTimestamp,
                        startTimestamp,
                    ) as ArrayList<Event>
                )
            )
        }
    }

    fun getEventTypes(): ArrayList<EventType> =
        runBlocking {
            withContext(IO) {
                database.EventTypesDao().getEventTypes().let {
                    ArrayList(it)
                }
            }
        }
}

sealed class WeekFragmentEvent {
    data class EventsLoaded(val events: ArrayList<Event>) : WeekFragmentEvent()
    object EventsUpdated : WeekFragmentEvent()
}