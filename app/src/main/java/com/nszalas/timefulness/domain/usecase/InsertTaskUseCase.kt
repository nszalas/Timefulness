package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.domain.model.Task
import com.nszalas.timefulness.repository.TaskRepository
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(
    private val repository: TaskRepository,
){
    suspend operator fun invoke(task: Task): Long = repository.insert(task)
}