package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.ui.TechniqueUIMapper
import com.nszalas.timefulness.repository.TechniqueRepository
import com.nszalas.timefulness.ui.model.TechniqueUI
import com.nszalas.timefulness.utils.DateTimeProvider
import javax.inject.Inject

class GetTechniqueForTodayUseCase @Inject constructor(
    private val techniqueRepository: TechniqueRepository,
    private val techniqueUIMapper: TechniqueUIMapper,
    private val dateTimeProvider: DateTimeProvider
) {
    suspend operator fun invoke(): TechniqueUI? {
        val date = dateTimeProvider.currentDate()
        val techniques = techniqueRepository.getAllTechniques()

        return if(techniques.isNotEmpty()) {
            val techniqueForToday = techniques[(date.dayOfMonth - 1) % techniques.count()]
            techniqueUIMapper(technique = techniqueForToday)
        } else { null }
    }
}