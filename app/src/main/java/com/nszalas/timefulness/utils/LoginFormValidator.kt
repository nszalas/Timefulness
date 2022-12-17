package com.nszalas.timefulness.utils

import javax.inject.Inject

class LoginFormValidator @Inject constructor() {
    fun isEmailValid(email: String?): Boolean {
        return !email.isNullOrBlank()
    }

    fun isPasswordValid(password: String?, confirmPassword: String? = null): Boolean =
        confirmPassword?.run {
            checkPassword(password) && isNotBlank() && password == confirmPassword
        } ?: checkPassword(password)

    private fun checkPassword(password: String?): Boolean {
        return !password.isNullOrBlank()
    }
}