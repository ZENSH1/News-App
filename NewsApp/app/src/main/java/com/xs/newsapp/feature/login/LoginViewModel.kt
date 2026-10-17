package com.xs.newsapp.feature.login

import androidx.lifecycle.viewModelScope
import com.xs.newsapp.core.mvi.MviViewModel
import com.xs.newsapp.core.validation.Validation
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : MviViewModel<LoginState, LoginIntent, LoginMutation, LoginEffect>(LoginState(), LoginReducer) {

    override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> mutate(LoginMutation.EmailUpdated(intent.email))
            is LoginIntent.PasswordChanged -> mutate(LoginMutation.PasswordUpdated(intent.password))
            LoginIntent.TogglePasswordVisibility -> mutate(LoginMutation.PasswordVisibilityToggled)
            LoginIntent.CreateAccountClicked -> sendEffect(LoginEffect.NavigateToSignUp)
            LoginIntent.Submit -> submit()
        }
    }

    private fun submit() {
        val state = currentState
        if (state.isSubmitting) return

        val emailError = Validation.email(state.email)
        val passwordError = Validation.existingPassword(state.password)
        if (emailError != null || passwordError != null) {
            mutate(LoginMutation.ValidationFailed(emailError, passwordError))
            return
        }

        mutate(LoginMutation.SubmissionStarted)
        viewModelScope.launch {
            try {
                // On success there is nothing to do here: the session changes and the app shell
                // (AppViewModel) switches to the logged-in screens.
                authRepository.signIn(state.email.trim(), state.password)
            } catch (e: BackendException) {
                sendEffect(LoginEffect.ShowError(e.error))
            } finally {
                mutate(LoginMutation.SubmissionFinished)
            }
        }
    }
}
