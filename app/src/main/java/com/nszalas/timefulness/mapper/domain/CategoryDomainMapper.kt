package com.nszalas.timefulness.mapper.domain

import com.nszalas.timefulness.domain.model.Category
import com.nszalas.timefulness.infrastructure.local.room.entity.CategoryEntity
import javax.inject.Inject

class CategoryDomainMapper @Inject constructor() {
    operator fun invoke(categoryEntity: CategoryEntity): Category = with(categoryEntity) {
        Category(
            id = id,
            name = name,
            colorMain = colorMain,
            colorText = colorText,
            emoji = emoji,
        )
    }
}