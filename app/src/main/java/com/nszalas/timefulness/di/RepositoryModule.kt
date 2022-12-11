package com.nszalas.timefulness.di

import com.nszalas.timefulness.infrastructure.local.TaskDao
import com.nszalas.timefulness.infrastructure.local.TipDao
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.repository.TipRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTaskRepository(
        taskDao: TaskDao
    ): TaskRepository = TaskRepository(taskDao)

//    @Provides
//    @Singleton
//    fun provideTipRepository(
//        tipDao: TipDao
//    ): TipRepository = TipRepository(tipDao)
}