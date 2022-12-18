package com.nszalas.timefulness.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyzdroizy.simpleandroidbarchart.model.ChartData
import com.nszalas.timefulness.domain.usecase.GetCurrentUserUseCase
import com.nszalas.timefulness.domain.usecase.GetTasksForWeekUseCase
import com.nszalas.timefulness.domain.usecase.GetTasksForDateUseCase
import com.nszalas.timefulness.domain.usecase.LogoutUserUseCase
import com.nszalas.timefulness.extensions.EventsChannel
import com.nszalas.timefulness.extensions.asLocalDateTime
import com.nszalas.timefulness.extensions.mutate
import com.nszalas.timefulness.ui.model.TaskWithCategoryUI
import com.nszalas.timefulness.utils.DateTimeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val currentUser: GetCurrentUserUseCase,
    private val dateTimeProvider: DateTimeProvider,
    private val getTasksForDate: GetTasksForDateUseCase,
    private val getTasksForWeek: GetTasksForWeekUseCase,
    private val logoutUser: LogoutUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileViewState())
    val state: StateFlow<ProfileViewState> = _state.asStateFlow()

    private val _event = EventsChannel<ProfileViewEvent>()
    val event: Flow<ProfileViewEvent> = _event.receiveAsFlow()

    init {
        loadCurrentUser()
    }

    fun onRefresh() {
        loadTodayTaskCompletion()
        loadStatistics()
    }

    fun onLogout() {
        viewModelScope.launch {
            _state.mutate { copy(isLoading = true) }
            try {
                if(logoutUser().isSuccess) {
                    _event.trySend(ProfileViewEvent.UserLoggedOut)
                }
            } catch (e: Exception) {
                _event.trySend(ProfileViewEvent.Error(e.message))
            }
            _state.mutate { copy(isLoading = false) }
        }
    }

    private fun loadCurrentUser() {
        val user = runBlocking(IO) { currentUser() }
        user ?: return

        viewModelScope.launch {
            _state.mutate {
                copy(
                    user = user,
                )
            }
        }
    }

    private fun loadTodayTaskCompletion() {
        val tasks = runBlocking(IO) { getTasksForDate(dateTimeProvider.currentDate()) }

        viewModelScope.launch {
            _state.mutate {
                copy(
                    taskAllCount = tasks.count(),
                    taskCompletedCount = tasks.count { it.task.completed }
                )
            }
        }
    }

    private fun loadStatistics() {
        val tasks = runBlocking(IO) { getTasksForWeek(dateTimeProvider.currentDate()) }
        val chartDataSet = runBlocking { generateChartData(tasks) }

        viewModelScope.launch {
            _state.mutate {
                copy(
                    statistics = chartDataSet,
                    percentage = calculatePercentage(chartDataSet)
                )
            }
        }
    }

    private fun generateChartData(tasks: List<TaskWithCategoryUI>): List<ChartData> =
        tasks.groupBy {
            it.task.startTimestamp.asLocalDateTime(it.task.timezoneId).dayOfWeek
        }
            .toSortedMap()
            .map { entries ->
            ChartData(
                label = entries.key.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                value = entries.value.count { task -> task.task.completed },
                maxValue = entries.value.count()
            )
        }

    private fun calculatePercentage(items: List<ChartData>): Int {
        return (items.sumOf { it.value.toDouble() } / items.sumOf { it.maxValue } * 100).toInt()
    }
}