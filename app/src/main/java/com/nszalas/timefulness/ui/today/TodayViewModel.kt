package com.nszalas.timefulness.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.infrastructure.local.entity.TaskEntity
import com.nszalas.timefulness.infrastructure.local.AppDatabase
import com.nszalas.timefulness.infrastructure.local.entity.TaskWithCategoryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val taskRepository: TaskRepository
): ViewModel() {

    fun insert(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.insert(task)
        }
    }

    fun delete(task: TaskEntity)
    {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.delete(task)
        }
    }

    fun update(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.update(task)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteAll()
        }
    }
}