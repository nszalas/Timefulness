package com.nszalas.timefulness.mapper.domain

import com.nszalas.timefulness.domain.model.Advice
import com.nszalas.timefulness.infrastructure.local.room.entity.AdviceEntity
import javax.inject.Inject

class AdviceDomainMapper @Inject constructor() {
    operator fun invoke(adviceEntity: AdviceEntity): Advice = with(adviceEntity) {
        Advice(
            id = id,
            description = description
        )
    }
}