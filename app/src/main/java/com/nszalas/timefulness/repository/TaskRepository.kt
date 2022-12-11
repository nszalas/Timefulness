package com.nszalas.timefulness.repository

import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.infrastructure.local.TaskDao
import com.nszalas.timefulness.infrastructure.local.entity.TaskWithCategoryEntity
import com.nszalas.timefulness.mapper.entity.TaskEntityMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val taskEntityMapper: TaskEntityMapper,
) {
    fun getAll(): Flow<List<TaskWithCategoryEntity>> = taskDao.getAll()

    suspend fun insert(task: Task) = taskDao.insert(taskEntityMapper(task))

    suspend fun deleteTaskWithId(taskId: Int) = taskDao.deleteTaskWithId(taskId)

    suspend fun deleteAll() = taskDao.deleteAll()
}