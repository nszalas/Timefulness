package com.nszalas.timefulness.di

import android.content.Context
import androidx.room.Room
import com.nszalas.timefulness.infrastructure.local.AppDatabase
import com.nszalas.timefulness.infrastructure.local.CategoryDao
import com.nszalas.timefulness.infrastructure.local.Constants.DATABASE_NAME
import com.nszalas.timefulness.infrastructure.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        DATABASE_NAME
    ).createFromAsset("databases/timefulness.db").build()

    @Provides
    @Singleton
    fun provideTaskDao(
        database: AppDatabase
    ): TaskDao = database.taskDao

    @Provides
    @Singleton
    fun provideCategoryDao(
        database: AppDatabase
    ): CategoryDao = database.categoryDao

}