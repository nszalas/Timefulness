package com.nszalas.timefulness.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.nszalas.timefulness.model.TaskRepository
import com.nszalas.timefulness.model.Task
import com.nszalas.timefulness.model.AppDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    database: AppDatabase,
): ViewModel() {
    val getAll: LiveData<List<Task>>
    private val taskRepository = TaskRepository(database.taskDao())

    init {
        getAll = taskRepository.getAll
    }

    fun insert(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.insert(task)
        }
    }

    fun delete(task: Task)
    {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.delete(task)
        }
    }

    fun update(task: Task) {
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