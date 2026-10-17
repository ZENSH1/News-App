package com.xs.newsapp.feature.signup

import androidx.lifecycle.viewModelScope
import com.xs.newsapp.core.mvi.MviViewModel
import com.xs.newsapp.core.validation.Validation
import com.xs.newsapp.data.model.BackendException
import com.xs.newsapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : MviViewModel<SignUpState, SignUpIntent, SignUpMutation, SignUpEffect>(SignUpState(), SignUpReducer) {

    override fun handleIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.NameChanged -> mutate(SignUpMutation.NameUpdated(intent.name))
            is SignUpIntent.EmailChanged -> mutate(SignUpMutation.EmailUpdated(intent.email))
            is SignUpIntent.PasswordChanged -> mutate(SignUpMutation.PasswordUpdated(intent.password))
            is SignUpIntent.ConfirmPasswordChanged ->
                mutate(SignUpMutation.ConfirmPasswordUpdated(intent.confirmPassword))
            SignUpIntent.TogglePasswordVisibility -> mutate(SignUpMutation.PasswordVisibilityToggled)
            SignUpIntent.LogInClicked -> sendEffect(SignUpEffect.NavigateBack)
            SignUpIntent.Submit -> submit()
        }
    }

    private fun submit() {
        val state = currentState
        if (state.isSubmitting) return

        val errors = SignUpErrors(
            name = Validation.required(state.name),
            email = Validation.email(state.email),
            password = Validation.newPassword(state.password),
            confirmPassword = Validation.passwordConfirmation(state.password, state.confirmPassword),
        )
        if (errors.hasAny) {
            mutate(SignUpMutation.ValidationFailed(errors))
            return
        }

        mutate(SignUpMutation.SubmissionStarted)
        viewModelScope.launch {
            try {
                authRepository.signUp(state.name.trim(), state.email.trim(), state.password)
            } catch (e: BackendException) {
                sendEffect(SignUpEffect.ShowError(e.error))
            } finally {
                mutate(SignUpMutation.SubmissionFinished)
            }
        }
    }
}
