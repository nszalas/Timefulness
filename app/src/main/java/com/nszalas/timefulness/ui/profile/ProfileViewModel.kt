package com.nszalas.timefulness.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boyzdroizy.simpleandroidbarchart.model.ChartData

class ProfileViewModel : ViewModel() {

    private val _state = MutableLiveData(ProfileViewState())
    val state: LiveData<ProfileViewState> = _state

    fun loadTodayTaskCompletion() {
        _state.value = _state.value?.copy(
            taskAllCount = 10,
            taskCompletedCount = 6
        )
    }

    fun loadStatistics() {
        val statistics = listOf(
            ChartData(label = "Pon", value = 3, maxValue = 6),
            ChartData(label = "Wt", value = 5, maxValue = 7),
            ChartData(label = "Śr", value = 5, maxValue = 5),
            ChartData(label = "Czw", value = 7, maxValue = 8),
            ChartData(label = "Pt", value = 7, maxValue = 7),
            ChartData(label = "So", value = 8, maxValue = 13),
            ChartData(label = "Nd", value = 3, maxValue = 4),
        )
        _state.value = _state.value?.copy(
            statistics = statistics,
            percentage = calculatePercentage(statistics),
        )
    }

    private fun calculatePercentage(items: List<ChartData>): Int {
        return (items.sumOf { it.value.toDouble() } / items.sumOf { it.maxValue } * 100).toInt()
    }
}