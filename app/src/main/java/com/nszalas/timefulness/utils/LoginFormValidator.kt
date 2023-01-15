package com.nszalas.timefulness.utils

import androidx.core.util.PatternsCompat
import com.nszalas.timefulness.domain.error.WeakPasswordException
import javax.inject.Inject

class LoginFormValidator @Inject constructor() {
    fun isEmailValid(email: String?): Boolean {
        return !email.isNullOrBlank() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String?, confirmPassword: String? = null): Boolean =
        confirmPassword?.run {
            checkPassword(password) && isNotBlank() && password == confirmPassword
        } ?: checkPassword(password)

    private fun checkPassword(password: String?): Boolean {
        return password?.let {
            if (it.length < 6) {
                throw WeakPasswordException()
            } else {
                true
            }
        } ?: false

    }
}