package com.nszalas.timefulness.infrastructure.local

import androidx.room.*
import com.nszalas.timefulness.infrastructure.local.Constants.COLUMN_TASK_ID
import com.nszalas.timefulness.infrastructure.local.Constants.TABLE_TASK
import com.nszalas.timefulness.infrastructure.local.entity.TaskEntity
import com.nszalas.timefulness.infrastructure.local.entity.TaskWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Transaction
    @Query("DELETE FROM $TABLE_TASK WHERE $COLUMN_TASK_ID = :taskId")
    suspend fun deleteTaskWithId(taskId: Int)

    @Query("DELETE FROM $TABLE_TASK")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TABLE_TASK")
    fun getAll(): Flow<List<TaskWithCategoryEntity>>
}