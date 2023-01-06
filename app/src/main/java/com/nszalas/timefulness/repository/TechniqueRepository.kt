package com.nszalas.timefulness.repository

import com.nszalas.timefulness.domain.model.Technique
import com.nszalas.timefulness.infrastructure.local.room.TechniqueDao
import com.nszalas.timefulness.mapper.domain.TechniqueDomainMapper
import javax.inject.Inject

class TechniqueRepository @Inject constructor(
    private val techniqueDao: TechniqueDao,
    private val mapper: TechniqueDomainMapper
) {
    suspend fun getAllTechniques(): List<Technique> =
        techniqueDao.getAllTechniques().map { mapper(it) }

    suspend fun getTechniqueWithId(techniqueId: Int): Technique =
        mapper(techniqueDao.getTechniqueWithId(techniqueId))
}