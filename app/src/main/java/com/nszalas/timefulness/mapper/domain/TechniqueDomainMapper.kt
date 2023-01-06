package com.nszalas.timefulness.mapper.domain

import com.nszalas.timefulness.domain.model.Technique
import com.nszalas.timefulness.infrastructure.local.room.entity.TechniqueEntity

class TechniqueDomainMapper {
    operator fun invoke(techniqueEntity: TechniqueEntity): Technique = with(techniqueEntity) {
        Technique(
            id = id,
            title = title,
            description = description
        )
    }
}