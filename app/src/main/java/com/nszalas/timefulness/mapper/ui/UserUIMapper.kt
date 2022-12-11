package com.nszalas.timefulness.mapper.ui

import com.nszalas.timefulness.domain.model.User
import com.nszalas.timefulness.ui.model.UserUI
import com.nszalas.timefulness.utils.PlaceholderPhotoProvider
import javax.inject.Inject

class UserUIMapper @Inject constructor(
    private val placeholderPhotoProvider: PlaceholderPhotoProvider,
) {
    operator fun invoke(user: User) = with(user) {
        val username = name?.let {
            it.takeIf { it.isNotEmpty() } ?: "User"
        } ?: "User"

        UserUI(
            id = id,
            email = email ?: "",
            name = username,
            photoUrl = photoUrl ?: placeholderPhotoProvider.getPhotoUrl(username)
        )
    }
}