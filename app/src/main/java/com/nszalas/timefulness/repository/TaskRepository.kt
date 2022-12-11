package com.nszalas.timefulness.repository

import com.nszalas.timefulness.infrastructure.local.TaskDao
import com.nszalas.timefulness.infrastructure.local.entity.TaskEntity
import com.nszalas.timefulness.infrastructure.local.entity.TaskWithCategoryEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository (private val appDao: TaskDao) {
    val getAll: Flow<List<TaskWithCategoryEntity>> = appDao.getAll()

    suspend fun insert(task: TaskEntity) {
        appDao.insert(task)
    }

    fun update(task: TaskEntity){
        appDao.update(task)
    }

    fun delete(task: TaskEntity){
        appDao.delete(task)
    }

    fun deleteAll(){
        appDao.deleteAll()
    }
}