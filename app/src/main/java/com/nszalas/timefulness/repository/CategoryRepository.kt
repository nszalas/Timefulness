package com.nszalas.timefulness.repository

import com.nszalas.timefulness.domain.model.Category
import com.nszalas.timefulness.infrastructure.local.room.CategoryDao
import com.nszalas.timefulness.mapper.domain.CategoryDomainMapper
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val mapper: CategoryDomainMapper,
) {
    suspend fun getCategories(): List<Category> = categoryDao.getCategories().map { mapper(it) }
}