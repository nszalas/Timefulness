package com.nszalas.timefulness.infrastructure.local

import androidx.room.*
import com.nszalas.timefulness.infrastructure.local.Constants.COLUMN_TASK_COMPLETED
import com.nszalas.timefulness.infrastructure.local.Constants.COLUMN_TASK_END_TIMESTAMP
import com.nszalas.timefulness.infrastructure.local.Constants.COLUMN_TASK_ID
import com.nszalas.timefulness.infrastructure.local.Constants.COLUMN_TASK_START_TIMESTAMP
import com.nszalas.timefulness.infrastructure.local.Constants.TABLE_TASK
import com.nszalas.timefulness.infrastructure.local.entity.TaskEntity
import com.nszalas.timefulness.infrastructure.local.entity.TaskWithCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Transaction
    @Query("DELETE FROM $TABLE_TASK WHERE $COLUMN_TASK_ID = :taskId")
    suspend fun deleteTaskWithId(taskId: Int)

    @Query("DELETE FROM $TABLE_TASK")
    suspend fun deleteAll()

    @Query("SELECT * FROM $TABLE_TASK " +
            "ORDER BY $COLUMN_TASK_COMPLETED DESC, $COLUMN_TASK_START_TIMESTAMP ASC")
    fun getAllTasks(): Flow<List<TaskWithCategoryEntity>>

    @Query("SELECT * FROM $TABLE_TASK " +
            "WHERE $COLUMN_TASK_START_TIMESTAMP >= :startTimestamp " +
            "AND $COLUMN_TASK_END_TIMESTAMP <= :endTimestamp " +
            "ORDER BY $COLUMN_TASK_COMPLETED DESC, $COLUMN_TASK_START_TIMESTAMP ASC")
    fun getTasksFromTo(startTimestamp: Long, endTimestamp: Long): Flow<List<TaskWithCategoryEntity>>
}