package com.xs.newsapp.feature.login

import com.xs.newsapp.core.mvi.Reducer
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.data.model.BackendError

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val emailError: FieldError? = null,
    val passwordError: FieldError? = null,
)

sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    data object TogglePasswordVisibility : LoginIntent
    data object Submit : LoginIntent
    data object CreateAccountClicked : LoginIntent
}

sealed interface LoginMutation {
    data class EmailUpdated(val email: String) : LoginMutation
    data class PasswordUpdated(val password: String) : LoginMutation
    data object PasswordVisibilityToggled : LoginMutation
    data class ValidationFailed(val emailError: FieldError?, val passwordError: FieldError?) : LoginMutation
    data object SubmissionStarted : LoginMutation
    data object SubmissionFinished : LoginMutation
}

sealed interface LoginEffect {
    data object NavigateToSignUp : LoginEffect
    data class ShowError(val error: BackendError) : LoginEffect
}

object LoginReducer : Reducer<LoginState, LoginMutation> {
    override fun reduce(state: LoginState, mutation: LoginMutation): LoginState = when (mutation) {
        // Editing a field clears its error.
        is LoginMutation.EmailUpdated -> state.copy(email = mutation.email, emailError = null)
        is LoginMutation.PasswordUpdated -> state.copy(password = mutation.password, passwordError = null)
        LoginMutation.PasswordVisibilityToggled -> state.copy(isPasswordVisible = !state.isPasswordVisible)
        is LoginMutation.ValidationFailed -> state.copy(
            emailError = mutation.emailError,
            passwordError = mutation.passwordError,
        )
        LoginMutation.SubmissionStarted -> state.copy(isSubmitting = true)
        LoginMutation.SubmissionFinished -> state.copy(isSubmitting = false)
    }
}
