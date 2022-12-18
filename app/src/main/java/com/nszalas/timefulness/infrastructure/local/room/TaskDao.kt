package com.nszalas.timefulness.infrastructure.local.room

import androidx.room.*
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_TASK_COMPLETED
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_TASK_END_TIMESTAMP
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_TASK_ID
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_TASK_START_TIMESTAMP
import com.nszalas.timefulness.infrastructure.local.room.Constants.COLUMN_TASK_USER_ID
import com.nszalas.timefulness.infrastructure.local.room.Constants.TABLE_TASK
import com.nszalas.timefulness.infrastructure.local.room.entity.TaskEntity
import com.nszalas.timefulness.infrastructure.local.room.entity.TaskWithCategoryEntity
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
            "AND $COLUMN_TASK_END_TIMESTAMP < :endTimestamp " +
            "AND $COLUMN_TASK_USER_ID == :userId " +
            "ORDER BY $COLUMN_TASK_COMPLETED DESC, $COLUMN_TASK_START_TIMESTAMP ASC")
    fun observeTasksFromTo(startTimestamp: Long, endTimestamp: Long, userId: String): Flow<List<TaskWithCategoryEntity>>

    @Query("SELECT * FROM $TABLE_TASK " +
            "WHERE $COLUMN_TASK_START_TIMESTAMP >= :startTimestamp " +
            "AND $COLUMN_TASK_END_TIMESTAMP < :endTimestamp " +
            "AND $COLUMN_TASK_USER_ID == :userId " +
            "ORDER BY $COLUMN_TASK_COMPLETED DESC, $COLUMN_TASK_START_TIMESTAMP ASC")
    suspend fun getTasksFromTo(startTimestamp: Long, endTimestamp: Long, userId: String): List<TaskWithCategoryEntity>
}