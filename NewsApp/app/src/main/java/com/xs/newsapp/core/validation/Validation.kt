package com.xs.newsapp.core.validation

enum class FieldError {
    Blank,
    InvalidEmail,
    PasswordTooShort,
    PasswordMismatch,
    InvalidAge,
    TooLong,
}

/** Pure input rules. */
object Validation {

    const val MIN_PASSWORD_LENGTH = 6
    const val MIN_AGE = 1
    const val MAX_AGE = 120
    const val MAX_HEADLINE_LENGTH = 120
    const val MAX_DESCRIPTION_LENGTH = 5_000

    private val EMAIL = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")

    fun required(value: String): FieldError? = if (value.isBlank()) FieldError.Blank else null

    fun email(value: String): FieldError? = when {
        value.isBlank() -> FieldError.Blank
        !EMAIL.matches(value.trim()) -> FieldError.InvalidEmail
        else -> null
    }

    /** The backend decides at sign-in. */
    fun existingPassword(value: String): FieldError? = if (value.isEmpty()) FieldError.Blank else null

    fun newPassword(value: String): FieldError? = when {
        value.isEmpty() -> FieldError.Blank
        value.length < MIN_PASSWORD_LENGTH -> FieldError.PasswordTooShort
        else -> null
    }

    fun passwordConfirmation(password: String, confirmation: String): FieldError? = when {
        confirmation.isEmpty() -> FieldError.Blank
        confirmation != password -> FieldError.PasswordMismatch
        else -> null
    }

    fun maxLength(value: String, max: Int): FieldError? = when {
        value.isBlank() -> FieldError.Blank
        value.trim().length > max -> FieldError.TooLong
        else -> null
    }

    /** Optional. */
    fun optionalAge(value: String): FieldError? {
        if (value.isBlank()) return null
        val age = value.trim().toIntOrNull() ?: return FieldError.InvalidAge
        return if (age in MIN_AGE..MAX_AGE) null else FieldError.InvalidAge
    }
}
