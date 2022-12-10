package com.nszalas.timefulness.utils

import com.nszalas.timefulness.R

class TaskNameValidator {
    fun isValid(name: String?): Int? {
        return when {
            name == null || name.isEmpty() -> R.string.add_error_task_name_invalid
            name.isNotEmpty() && name.length < 3 -> R.string.add_error_task_name_short
            name.isNotEmpty() && name.length > 40 -> R.string.add_error_task_name_long
            name.isNotEmpty() && name.hasInvalidCharacters() -> R.string.add_error_task_name_invalid_characters
            else -> null
        }
    }

    private fun String.hasInvalidCharacters(): Boolean =
        !this.matches("^[\\w\\-\\s]+$".toRegex())
}