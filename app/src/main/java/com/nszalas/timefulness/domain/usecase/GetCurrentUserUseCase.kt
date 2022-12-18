package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.mapper.ui.UserUIMapper
import com.nszalas.timefulness.repository.AuthenticationRepository
import com.nszalas.timefulness.ui.model.UserUI
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthenticationRepository,
    private val mapper: UserUIMapper,
) {
    operator fun invoke(): UserUI? = repository.getCurrentUser()?.let { mapper(it) }
}