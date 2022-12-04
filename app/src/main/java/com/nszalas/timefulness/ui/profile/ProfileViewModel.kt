package com.nszalas.timefulness.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boyzdroizy.simpleandroidbarchart.model.ChartData
import com.nszalas.timefulness.extensions.mutate
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileViewState())
    val state: StateFlow<ProfileViewState> = _state.asStateFlow()

    init {
        loadTodayTaskCompletion()
        loadStatistics()
    }

    private fun loadTodayTaskCompletion() {
        viewModelScope.launch(IO) {
            _state.mutate {
                copy(
                    taskAllCount = 10,
                    taskCompletedCount = 6
                )
            }
        }
    }

    private fun loadStatistics() {
        val statistics = listOf(
            ChartData(label = "Pon", value = 3, maxValue = 6),
            ChartData(label = "Wt", value = 5, maxValue = 7),
            ChartData(label = "Śr", value = 5, maxValue = 5),
            ChartData(label = "Czw", value = 7, maxValue = 8),
            ChartData(label = "Pt", value = 7, maxValue = 7),
            ChartData(label = "So", value = 8, maxValue = 13),
            ChartData(label = "Nd", value = 3, maxValue = 4),
        )
        viewModelScope.launch(IO) {
            _state.mutate {
                copy(
                    statistics = statistics,
                    percentage = calculatePercentage(statistics),
                )
            }
        }
    }

    private fun calculatePercentage(items: List<ChartData>): Int {
        return (items.sumOf { it.value.toDouble() } / items.sumOf { it.maxValue } * 100).toInt()
    }
}