package com.xs.newsapp.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.xs.newsapp.R
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.core.validation.Validation
import com.xs.newsapp.data.model.BackendError

// Stores and reducers speak in error *types*; turning them into words is the View's job.

@StringRes
fun BackendError.messageRes(): Int = when (this) {
    BackendError.InvalidCredentials -> R.string.error_invalid_credentials
    BackendError.EmailAlreadyInUse -> R.string.error_email_in_use
    BackendError.WeakPassword -> R.string.error_weak_password
    BackendError.Network -> R.string.error_network
    BackendError.PermissionDenied -> R.string.error_permission_denied
    BackendError.NotFound -> R.string.error_not_found
    BackendError.Unknown -> R.string.error_unknown
}

/** @param maxLength the limit to mention for [FieldError.TooLong]. */
@Composable
fun FieldError.message(maxLength: Int = 0): String = when (this) {
    FieldError.Blank -> stringResource(R.string.error_required)
    FieldError.InvalidEmail -> stringResource(R.string.error_email_invalid)
    FieldError.PasswordTooShort -> pluralStringResource(
        R.plurals.error_password_too_short,
        Validation.MIN_PASSWORD_LENGTH,
        Validation.MIN_PASSWORD_LENGTH,
    )
    FieldError.PasswordMismatch -> stringResource(R.string.error_password_mismatch)
    FieldError.InvalidAge -> stringResource(R.string.error_age_invalid, Validation.MIN_AGE, Validation.MAX_AGE)
    FieldError.TooLong -> pluralStringResource(R.plurals.error_too_long, maxLength, maxLength)
}
