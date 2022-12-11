package com.nszalas.timefulness.di

import com.nszalas.timefulness.mapper.domain.CategoryDomainMapper
import com.nszalas.timefulness.mapper.domain.UserDomainMapper
import com.nszalas.timefulness.mapper.entity.TaskEntityMapper
import com.nszalas.timefulness.mapper.ui.CategoryUIMapper
import com.nszalas.timefulness.mapper.ui.UserUIMapper
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
}