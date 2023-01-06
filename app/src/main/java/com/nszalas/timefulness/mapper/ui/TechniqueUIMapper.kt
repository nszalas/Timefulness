package com.nszalas.timefulness.mapper.ui

import com.nszalas.timefulness.domain.model.Technique
import com.nszalas.timefulness.ui.model.TechniqueUI

class TechniqueUIMapper {
    operator fun invoke(technique: Technique): TechniqueUI = with(technique) {
        TechniqueUI(
            title = title,
            description = description,
        )
    }
}