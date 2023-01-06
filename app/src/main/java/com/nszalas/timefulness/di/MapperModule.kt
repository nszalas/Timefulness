package com.nszalas.timefulness.di

import com.nszalas.timefulness.mapper.domain.*
import com.nszalas.timefulness.mapper.entity.TaskEntityMapper
import com.nszalas.timefulness.mapper.ui.*
import com.nszalas.timefulness.utils.PlaceholderPhotoProvider
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class MapperModule {

    // category

    @Provides
    @Reusable
    fun provideCategoryDomainMapper() = CategoryDomainMapper()

    @Provides
    @Reusable
    fun provideCategoryUIMapper() = CategoryUIMapper()

    // user

    @Provides
    @Reusable
    fun provideUserDomainMapper() = UserDomainMapper()

    @Provides
    @Reusable
    fun provideUserUIMapper(
        placeholderPhotoProvider: PlaceholderPhotoProvider
    ) = UserUIMapper(placeholderPhotoProvider)

    // task

    @Provides
    @Reusable
    fun provideTaskEntityMapper() = TaskEntityMapper()

    @Provides
    @Reusable
    fun provideTaskDomainMapper() = TaskDomainMapper()

    @Provides
    @Reusable
    fun provideTaskUIMapper() = TaskUIMapper()

    @Provides
    @Reusable
    fun provideTaskFromUIMapper() = TaskFromUIMapper()

    // task with category

    @Provides
    @Reusable
    fun provideTaskWithCategoryDomainMapper(
        taskMapper: TaskDomainMapper,
        categoryMapper: CategoryDomainMapper,
    ) = TaskWithCategoryDomainMapper(taskMapper, categoryMapper)

    @Provides
    @Reusable
    fun provideTaskWithCategoryUIMapper(
        taskUIMapper: TaskUIMapper,
        categoryUIMapper: CategoryUIMapper
    ) = TaskWithCategoryUIMapper(taskUIMapper, categoryUIMapper)

    // advice

    @Provides
    @Reusable
    fun provideAdviceDomainMapper() = AdviceDomainMapper()

    @Provides
    @Reusable
    fun provideAdviceUIMapper() = AdviceUIMapper()

    // technique

    @Provides
    @Reusable
    fun provideTechniqueDomainMapper() = TechniqueDomainMapper()

    @Provides
    @Reusable
    fun provideTechniqueUIMapper() = TechniqueUIMapper()
}