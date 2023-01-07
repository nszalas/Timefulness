package com.nszalas.timefulness.repository

import com.nszalas.timefulness.infrastructure.local.LocalPermissionsDataSource
import javax.inject.Inject

class ApplicationSettingsRepository @Inject constructor(
    private val localPermissionsDataSource: LocalPermissionsDataSource
) {
    fun hasNotificationPermissions(): Boolean =
        localPermissionsDataSource.hasNotificationPermission()
}