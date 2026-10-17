package com.xs.newsapp.feature.login

import app.cash.turbine.test
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.testing.MainDispatcherRule
import com.xs.newsapp.testing.TestAuthRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = TestAuthRepository()

    private fun createViewModel() = LoginViewModel(authRepository)

    private fun LoginViewModel.fillIn(email: String = "ayesha@newsapp.test", password: String = "password") {
        onIntent(LoginIntent.EmailChanged(email))
        onIntent(LoginIntent.PasswordChanged(password))
    }

    @Test
    fun invalidInput_showsFieldErrors_withoutCallingTheBackend() = runTest {
        val viewModel = createViewModel()
        viewModel.fillIn(email = "not-an-email", password = "")

        viewModel.onIntent(LoginIntent.Submit)

        val state = viewModel.state.value
        assertEquals(FieldError.InvalidEmail, state.emailError)
        assertEquals(FieldError.Blank, state.passwordError)
        assertFalse(state.isSubmitting)
        assertTrue(authRepository.signInCalls.isEmpty())
    }

    @Test
    fun validInput_signsInWithTheTrimmedEmail() = runTest {
        val gate = CompletableDeferred<Unit>().also { authRepository.gate = it }
        val viewModel = createViewModel()
        viewModel.fillIn(email = "  ayesha@newsapp.test ")

        viewModel.onIntent(LoginIntent.Submit)
        assertTrue(viewModel.state.value.isSubmitting)

        gate.complete(Unit)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isSubmitting)
        assertEquals(listOf("ayesha@newsapp.test" to "password"), authRepository.signInCalls)
    }

    @Test
    fun submittingAgainWhileSigningIn_isIgnored() = runTest {
        authRepository.gate = CompletableDeferred()
        val viewModel = createViewModel()
        viewModel.fillIn()

        viewModel.onIntent(LoginIntent.Submit)
        viewModel.onIntent(LoginIntent.Submit)

        assertEquals(1, authRepository.signInCalls.size)
    }

    @Test
    fun backendFailure_isShownOnce_asAnEffect() = runTest {
        authRepository.failure = BackendError.InvalidCredentials
        val viewModel = createViewModel()
        viewModel.fillIn(password = "wrong")

        viewModel.effects.test {
            viewModel.onIntent(LoginIntent.Submit)
            assertEquals(LoginEffect.ShowError(BackendError.InvalidCredentials), awaitItem())
        }
        assertFalse(viewModel.state.value.isSubmitting)
    }

    @Test
    fun createAccount_navigatesToSignUp() = runTest {
        val viewModel = createViewModel()

        viewModel.effects.test {
            viewModel.onIntent(LoginIntent.CreateAccountClicked)
            assertEquals(LoginEffect.NavigateToSignUp, awaitItem())
        }
    }
}
