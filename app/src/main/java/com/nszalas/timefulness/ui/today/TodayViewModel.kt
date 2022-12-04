package com.nszalas.timefulness.ui.today

import android.app.Application
import android.media.CamcorderProfile.getAll
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.nszalas.timefulness.model.TaskRepository
import com.nszalas.timefulness.model.Task
import com.nszalas.timefulness.model.AppDatabase

class TodayViewModel (application: Application): AndroidViewModel(application) {
    val getAll: LiveData<List<Task>>
    private val taskRepository: TaskRepository

    init {
        val appDao = AppDatabase.getInstance(application).taskDao()
        taskRepository = TaskRepository(appDao)
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