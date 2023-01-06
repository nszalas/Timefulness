package com.nszalas.timefulness.di

import android.content.Context
import androidx.room.Room
import com.nszalas.timefulness.infrastructure.local.room.*
import com.nszalas.timefulness.infrastructure.local.room.Constants.DATABASE_NAME
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

    @Provides
    @Singleton
    fun provideAdviceDao(
        database: AppDatabase
    ): AdviceDao = database.adviceDao

    @Provides
    @Singleton
    fun provideTechniqueDao(
        database: AppDatabase
    ): TechniqueDao = database.techniqueDao

}