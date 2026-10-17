package com.xs.newsapp.feature.login

import com.xs.newsapp.core.mvi.Reducer
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.data.model.BackendError

/** Everything the login screen shows. Immutable: a new instance for every change. */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isSubmitting: Boolean = false,
    val emailError: FieldError? = null,
    val passwordError: FieldError? = null,
)

/** What the user can do on the login screen. */
sealed interface LoginIntent {
    data class EmailChanged(val email: String) : LoginIntent
    data class PasswordChanged(val password: String) : LoginIntent
    data object TogglePasswordVisibility : LoginIntent
    data object Submit : LoginIntent
    data object CreateAccountClicked : LoginIntent
}

/** Facts produced while handling intents. Only the reducer turns them into state. */
sealed interface LoginMutation {
    data class EmailUpdated(val email: String) : LoginMutation
    data class PasswordUpdated(val password: String) : LoginMutation
    data object PasswordVisibilityToggled : LoginMutation
    data class ValidationFailed(val emailError: FieldError?, val passwordError: FieldError?) : LoginMutation
    data object SubmissionStarted : LoginMutation
    data object SubmissionFinished : LoginMutation
}

/** One-off events for the View. Not state, because they must not replay after rotation. */
sealed interface LoginEffect {
    data object NavigateToSignUp : LoginEffect
    data class ShowError(val error: BackendError) : LoginEffect
}

object LoginReducer : Reducer<LoginState, LoginMutation> {
    override fun reduce(state: LoginState, mutation: LoginMutation): LoginState = when (mutation) {
        // Editing a field clears its error: the user is already fixing it.
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
