package com.nszalas.timefulness.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM task_table")
    fun deleteAll()

    @Query("SELECT * FROM task_table")
    fun getAll(): LiveData<List<Task>>
}