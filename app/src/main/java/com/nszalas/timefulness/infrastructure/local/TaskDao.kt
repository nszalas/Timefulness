package com.nszalas.timefulness.infrastructure.local

import androidx.room.*
import com.nszalas.timefulness.infrastructure.local.Constants.TABLE_TASK
import com.nszalas.timefulness.infrastructure.local.entity.TaskEntity
import com.nszalas.timefulness.infrastructure.local.entity.TaskWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: TaskEntity)

    @Update
    fun update(task: TaskEntity)

    @Delete
    fun delete(task: TaskEntity)

    @Query("DELETE FROM $TABLE_TASK")
    fun deleteAll()

    @Query("SELECT * FROM $TABLE_TASK")
    fun getAll(): Flow<List<TaskWithCategoryEntity>>
}