package com.nszalas.timefulness.domain.model

data class User(
    val id: String,
    val email: String?,
    val name: String?,
    val photoUrl: String?,
)