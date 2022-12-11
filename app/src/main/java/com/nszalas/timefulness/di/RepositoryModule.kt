package com.nszalas.timefulness.di

import com.nszalas.timefulness.infrastructure.local.CategoryDao
import com.nszalas.timefulness.infrastructure.local.TaskDao
import com.nszalas.timefulness.infrastructure.local.TipDao
import com.nszalas.timefulness.infrastructure.remote.RemoteFirebaseDataSource
import com.nszalas.timefulness.mapper.domain.CategoryDomainMapper
import com.nszalas.timefulness.mapper.domain.UserDomainMapper
import com.nszalas.timefulness.repository.CategoryRepository
import com.nszalas.timefulness.repository.FirebaseRepository
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

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryDao: CategoryDao,
        mapper: CategoryDomainMapper,
    ): CategoryRepository = CategoryRepository(categoryDao, mapper)

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        dataSource: RemoteFirebaseDataSource,
        mapper: UserDomainMapper,
    ): FirebaseRepository = FirebaseRepository(dataSource, mapper)

//    @Provides
//    @Singleton
//    fun provideTipRepository(
//        tipDao: TipDao
//    ): TipRepository = TipRepository(tipDao)
}