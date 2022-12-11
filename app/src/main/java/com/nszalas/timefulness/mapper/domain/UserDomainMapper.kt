package com.nszalas.timefulness.mapper.domain

import com.google.firebase.auth.FirebaseUser
import com.nszalas.timefulness.domain.model.User

class UserDomainMapper {
    operator fun invoke(user: FirebaseUser) = with(user) {
        User(
            id = uid,
            email = email,
            name = displayName,
            photoUrl = photoUrl?.toString()
        )
    }
}