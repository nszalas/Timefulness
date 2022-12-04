package com.nszalas.timefulness.model

import androidx.lifecycle.LiveData

class TaskRepository (private val appDao: TaskDao) {
    val getAll: LiveData<List<Task>> = appDao.getAll()

    suspend fun insert(task: Task) {
        appDao.insert(task)
    }

    fun update(task: Task){
        appDao.update(task)
    }

    fun delete(task: Task){
        appDao.delete(task)
    }

    fun deleteAll(){
        appDao.deleteAll()
    }
}