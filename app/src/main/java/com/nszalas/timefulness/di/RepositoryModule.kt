package com.nszalas.timefulness.di

import com.nszalas.timefulness.infrastructure.local.LocalNotificationDataSource
import com.nszalas.timefulness.infrastructure.local.LocalSchedulingDataSource
import com.nszalas.timefulness.infrastructure.local.room.CategoryDao
import com.nszalas.timefulness.infrastructure.local.room.TaskDao
import com.nszalas.timefulness.infrastructure.remote.RemoteFirebaseDataSource
import com.nszalas.timefulness.mapper.domain.CategoryDomainMapper
import com.nszalas.timefulness.mapper.domain.TaskWithCategoryDomainMapper
import com.nszalas.timefulness.mapper.domain.UserDomainMapper
import com.nszalas.timefulness.mapper.entity.TaskEntityMapper
import com.nszalas.timefulness.repository.CategoryRepository
import com.nszalas.timefulness.repository.AuthenticationRepository
import com.nszalas.timefulness.repository.NotificationRepository
import com.nszalas.timefulness.repository.TaskRepository
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
        taskDao: TaskDao,
        taskEntityMapper: TaskEntityMapper,
        taskWithCategoryDomainMapper: TaskWithCategoryDomainMapper
    ): TaskRepository = TaskRepository(taskDao, taskEntityMapper, taskWithCategoryDomainMapper)

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
    ): AuthenticationRepository = AuthenticationRepository(dataSource, mapper)

    @Provides
    @Singleton
    fun provideNotificationRepository(
        schedulingDataSource: LocalSchedulingDataSource,
        notificationDataSource: LocalNotificationDataSource
    ) = NotificationRepository(schedulingDataSource, notificationDataSource)
}