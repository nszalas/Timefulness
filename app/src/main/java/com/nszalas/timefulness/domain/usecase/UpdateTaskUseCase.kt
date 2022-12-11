package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.domain.TaskFromUIMapper
import com.nszalas.timefulness.repository.TaskRepository
import com.nszalas.timefulness.ui.model.TaskUI
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
    private val mapper: TaskFromUIMapper,
){
    suspend operator fun invoke(task: TaskUI) = repository.update(mapper(task))
}