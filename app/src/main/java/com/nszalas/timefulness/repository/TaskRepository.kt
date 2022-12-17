package com.nszalas.timefulness.repository

import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.domain.model.TaskWithCategory
import com.nszalas.timefulness.infrastructure.local.TaskDao
import com.nszalas.timefulness.mapper.domain.TaskWithCategoryDomainMapper
import com.nszalas.timefulness.mapper.entity.TaskEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val taskEntityMapper: TaskEntityMapper,
    private val taskCategoryMapper: TaskWithCategoryDomainMapper,
) {
    fun observeAllTasks(): Flow<List<TaskWithCategory>> = taskDao.getAllTasks().map { list ->
        list.map { taskCategoryMapper(it) }
    }

    fun observeTasksForUserBetweenTimestamps(
        startTimestamp: Long,
        endTimestamp: Long,
        userId: String
    ): Flow<List<TaskWithCategory>> =
        taskDao.observeTasksFromTo(startTimestamp, endTimestamp, userId).map { list ->
            list.map { taskCategoryMapper(it) }
        }

    suspend fun getTasksForUserBetweenTimestamps(
        startTimestamp: Long,
        endTimestamp: Long,
        userId: String
    ): List<TaskWithCategory> =
        taskDao.getTasksFromTo(startTimestamp, endTimestamp, userId).map { task ->
             taskCategoryMapper(task)
        }

    suspend fun insert(task: Task) = taskDao.insert(taskEntityMapper(task))

    suspend fun update(task: Task) = taskDao.update(taskEntityMapper(task))

    suspend fun deleteTaskWithId(taskId: Int) = taskDao.deleteTaskWithId(taskId)

    suspend fun deleteAll() = taskDao.deleteAll()
}