package com.nszalas.timefulness.infrastructure.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nszalas.timefulness.infrastructure.local.room.entity.CategoryEntity
import com.nszalas.timefulness.infrastructure.local.room.entity.TaskEntity

@Database(entities = [TaskEntity::class, CategoryEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val categoryDao: CategoryDao
}