package com.nszalas.timefulness.infrastructure.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nszalas.timefulness.infrastructure.local.room.entity.AdviceEntity
import com.nszalas.timefulness.infrastructure.local.room.entity.CategoryEntity
import com.nszalas.timefulness.infrastructure.local.room.entity.TaskEntity
import com.nszalas.timefulness.infrastructure.local.room.entity.TechniqueEntity

@Database(
    entities = [
        TaskEntity::class,
        CategoryEntity::class,
        AdviceEntity::class,
        TechniqueEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val categoryDao: CategoryDao
    abstract val adviceDao: AdviceDao
    abstract val techniqueDao: TechniqueDao
}