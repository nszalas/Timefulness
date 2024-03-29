package com.nszalas.timefulness.mapper.ui

import android.graphics.Color
import android.graphics.ColorSpace
import androidx.core.graphics.alpha
import androidx.core.graphics.convertTo
import com.nszalas.timefulness.domain.model.Category
import com.nszalas.timefulness.ui.model.CategoryUI

class CategoryUIMapper {
    operator fun invoke(category: Category): CategoryUI = with(category) {
        CategoryUI(
            id = id,
            name = name,
            colorMain = Color.parseColor(colorMain),
            colorText = Color.parseColor(colorText),
            emoji = emoji,
        )
    }
}