package com.nszalas.timefulness.di

import com.nszalas.timefulness.infrastructure.local.LocalNotificationDataSource
import com.nszalas.timefulness.infrastructure.local.LocalPermissionsDataSource
import com.nszalas.timefulness.infrastructure.local.LocalSchedulingDataSource
import com.nszalas.timefulness.infrastructure.local.room.AdviceDao
import com.nszalas.timefulness.infrastructure.local.room.CategoryDao
import com.nszalas.timefulness.infrastructure.local.room.TaskDao
import com.nszalas.timefulness.infrastructure.local.room.TechniqueDao
import com.nszalas.timefulness.infrastructure.remote.RemoteFirebaseDataSource
import com.nszalas.timefulness.mapper.domain.*
import com.nszalas.timefulness.mapper.entity.TaskEntityMapper
import com.nszalas.timefulness.repository.*
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
    fun provideApplicationSettingsRepository(
        localPermissionsDataSource: LocalPermissionsDataSource
    ) = ApplicationSettingsRepository(localPermissionsDataSource)

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

    @Provides
    @Singleton
    fun provideAdviceRepository(
        adviceDao: AdviceDao,
        adviceDomainMapper: AdviceDomainMapper
    ) = AdviceRepository(adviceDao, adviceDomainMapper)

    @Provides
    @Singleton
    fun provideTechniqueRepository(
        techniqueDao: TechniqueDao,
        techniqueDomainMapper: TechniqueDomainMapper
    ) = TechniqueRepository(techniqueDao, techniqueDomainMapper)
}