package com.nszalas.timefulness.utils

class LoginFormValidator {
    fun validateEmail(email: String): Boolean {
        return email.isNotBlank()
    }

    fun validatePassword(password: String, confirmPassword: String? = null): Boolean =
        confirmPassword?.run {
            checkPassword(password) && isNotBlank() && password == confirmPassword
        } ?: checkPassword(password)

    private fun checkPassword(password: String): Boolean {
        return password.isNotBlank()
    }
}