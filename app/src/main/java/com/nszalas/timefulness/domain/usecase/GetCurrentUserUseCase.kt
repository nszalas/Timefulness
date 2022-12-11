package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.ui.UserUIMapper
import com.nszalas.timefulness.repository.FirebaseRepository
import com.nszalas.timefulness.ui.model.UserUI
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: FirebaseRepository,
    private val mapper: UserUIMapper,
) {
    operator fun invoke(): UserUI? = repository.getCurrentUser()?.let { mapper(it) }
}