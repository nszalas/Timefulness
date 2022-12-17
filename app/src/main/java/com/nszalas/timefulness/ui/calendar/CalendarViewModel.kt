package com.nszalas.timefulness.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nszalas.timefulness.ui.calendar.room.EventsDatabase
import com.nszalas.timefulness.ui.calendar.room.models.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val database: EventsDatabase
) : ViewModel() {

    fun addEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            database.EventsDao().insertOrUpdate(event)
        }
    }
}