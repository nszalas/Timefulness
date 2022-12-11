package com.nszalas.timefulness.ui.today

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.nszalas.timefulness.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val taskRepository: TaskRepository
): ViewModel() {

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteAll()
        }
    }
}