package com.nszalas.timefulness.domain.usecase

import com.nszalas.timefulness.repository.ApplicationSettingsRepository
import javax.inject.Inject

class HasNotificationPermissionUseCase @Inject constructor(
    private val repository: ApplicationSettingsRepository
) {
    operator fun invoke(): Boolean = repository.hasNotificationPermissions()
}