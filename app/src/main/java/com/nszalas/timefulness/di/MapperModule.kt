package com.nszalas.timefulness.di

import com.nszalas.timefulness.mapper.domain.CategoryDomainMapper
import com.nszalas.timefulness.mapper.ui.CategoryUIMapper
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class MapperModule {

    @Provides
    @Reusable
    fun provideCategoryDomainMapper() = CategoryDomainMapper()

    @Provides
    @Reusable
    fun provideCategoryUIMapper() = CategoryUIMapper()
}