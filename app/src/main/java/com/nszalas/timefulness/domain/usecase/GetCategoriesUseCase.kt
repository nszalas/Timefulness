package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.ui.CategoryUIMapper
import com.nszalas.timefulness.repository.CategoryRepository
import com.nszalas.timefulness.ui.model.CategoryUI
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val mapper: CategoryUIMapper
) {
    suspend operator fun invoke(): List<CategoryUI> =
        categoryRepository.getCategories().map { mapper(it) }
}