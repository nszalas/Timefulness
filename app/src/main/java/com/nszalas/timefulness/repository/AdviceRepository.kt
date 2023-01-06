package com.nszalas.timefulness.repository

import com.nszalas.timefulness.domain.model.Advice
import com.nszalas.timefulness.infrastructure.local.room.AdviceDao
import com.nszalas.timefulness.infrastructure.local.room.entity.AdviceEntity
import com.nszalas.timefulness.mapper.domain.AdviceDomainMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AdviceRepository @Inject constructor(
    private val adviceDao: AdviceDao,
    private val mapper: AdviceDomainMapper
) {
    suspend fun getAll(): List<Advice> = adviceDao.getAll().map { mapper(it) }

    suspend fun getAdviceWithId(adviceId: Int): Advice =
        mapper(adviceDao.getAdviceWithId(adviceId))
}