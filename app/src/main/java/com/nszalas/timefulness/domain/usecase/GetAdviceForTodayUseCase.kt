package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.ui.AdviceUIMapper
import com.nszalas.timefulness.repository.AdviceRepository
import com.nszalas.timefulness.ui.model.AdviceUI
import com.nszalas.timefulness.utils.DateTimeProvider
import javax.inject.Inject

class GetAdviceForTodayUseCase @Inject constructor(
    private val adviceRepository: AdviceRepository,
    private val adviceUIMapper: AdviceUIMapper,
    private val dateTimeProvider: DateTimeProvider,
) {
    suspend operator fun invoke(): AdviceUI? {
        val date = dateTimeProvider.currentDate()
        val advices = adviceRepository.getAll()

        return if(advices.isNotEmpty()) {
            val adviceForToday = advices[(date.dayOfMonth - 1) % advices.count()]
            adviceUIMapper(advice = adviceForToday)
        } else { null }
    }
}