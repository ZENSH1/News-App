package com.xs.newsapp.feature.signup

import com.xs.newsapp.core.mvi.Reducer
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.data.model.BackendError

data class SignUpState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val errors: SignUpErrors = SignUpErrors(),
)

data class SignUpErrors(
    val name: FieldError? = null,
    val email: FieldError? = null,
    val password: FieldError? = null,
    val confirmPassword: FieldError? = null,
) {
    val hasAny: Boolean get() = name != null || email != null || password != null || confirmPassword != null
}

sealed interface SignUpIntent {
    data class NameChanged(val name: String) : SignUpIntent
    data class EmailChanged(val email: String) : SignUpIntent
    data class PasswordChanged(val password: String) : SignUpIntent
    data class ConfirmPasswordChanged(val confirmPassword: String) : SignUpIntent
    data object TogglePasswordVisibility : SignUpIntent
    data object Submit : SignUpIntent
    data object LogInClicked : SignUpIntent
}

sealed interface SignUpMutation {
    data class NameUpdated(val name: String) : SignUpMutation
    data class EmailUpdated(val email: String) : SignUpMutation
    data class PasswordUpdated(val password: String) : SignUpMutation
    data class ConfirmPasswordUpdated(val confirmPassword: String) : SignUpMutation
    data object PasswordVisibilityToggled : SignUpMutation
    data class ValidationFailed(val errors: SignUpErrors) : SignUpMutation
    data object SubmissionStarted : SignUpMutation
    data object SubmissionFinished : SignUpMutation
}

sealed interface SignUpEffect {
    data object NavigateBack : SignUpEffect
    data class ShowError(val error: BackendError) : SignUpEffect
}

object SignUpReducer : Reducer<SignUpState, SignUpMutation> {
    override fun reduce(state: SignUpState, mutation: SignUpMutation): SignUpState = when (mutation) {
        is SignUpMutation.NameUpdated ->
            state.copy(name = mutation.name, errors = state.errors.copy(name = null))
        is SignUpMutation.EmailUpdated ->
            state.copy(email = mutation.email, errors = state.errors.copy(email = null))
        // Changing the password also invalidates a "passwords don't match" error.
        is SignUpMutation.PasswordUpdated ->
            state.copy(password = mutation.password, errors = state.errors.copy(password = null, confirmPassword = null))
        is SignUpMutation.ConfirmPasswordUpdated ->
            state.copy(confirmPassword = mutation.confirmPassword, errors = state.errors.copy(confirmPassword = null))
        SignUpMutation.PasswordVisibilityToggled -> state.copy(isPasswordVisible = !state.isPasswordVisible)
        is SignUpMutation.ValidationFailed -> state.copy(errors = mutation.errors)
        SignUpMutation.SubmissionStarted -> state.copy(isSubmitting = true)
        SignUpMutation.SubmissionFinished -> state.copy(isSubmitting = false)
    }
}
