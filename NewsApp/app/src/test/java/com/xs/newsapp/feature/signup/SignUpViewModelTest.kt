package com.xs.newsapp.feature.signup

import app.cash.turbine.test
import com.xs.newsapp.core.validation.FieldError
import com.xs.newsapp.data.model.BackendError
import com.xs.newsapp.testing.MainDispatcherRule
import com.xs.newsapp.testing.TestAuthRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SignUpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = TestAuthRepository()

    private fun createViewModel() = SignUpViewModel(authRepository)

    private fun SignUpViewModel.fillIn(
        name: String = "Sana Malik",
        email: String = "sana@newsapp.test",
        password: String = "secret1",
        confirmPassword: String = password,
    ) {
        onIntent(SignUpIntent.NameChanged(name))
        onIntent(SignUpIntent.EmailChanged(email))
        onIntent(SignUpIntent.PasswordChanged(password))
        onIntent(SignUpIntent.ConfirmPasswordChanged(confirmPassword))
    }

    @Test
    fun everyFieldIsValidated() = runTest {
        val viewModel = createViewModel()
        viewModel.fillIn(name = " ", email = "sana@", password = "123", confirmPassword = "1234")

        viewModel.onIntent(SignUpIntent.Submit)

        assertEquals(
            SignUpErrors(
                name = FieldError.Blank,
                email = FieldError.InvalidEmail,
                password = FieldError.PasswordTooShort,
                confirmPassword = FieldError.PasswordMismatch,
            ),
            viewModel.state.value.errors,
        )
        assertTrue(authRepository.signUpCalls.isEmpty())
    }

    @Test
    fun changingThePassword_alsoClearsTheMismatchError() {
        val state = SignUpState(
            errors = SignUpErrors(password = FieldError.PasswordTooShort, confirmPassword = FieldError.PasswordMismatch),
        )

        val next = SignUpReducer.reduce(state, SignUpMutation.PasswordUpdated("a-longer-password"))

        assertEquals(SignUpErrors(), next.errors)
    }

    @Test
    fun validForm_createsTheAccountWithTrimmedDetails() = runTest {
        val viewModel = createViewModel()
        viewModel.fillIn(name = " Sana Malik ", email = " sana@newsapp.test ")

        viewModel.onIntent(SignUpIntent.Submit)

        assertEquals(listOf(Triple("Sana Malik", "sana@newsapp.test", "secret1")), authRepository.signUpCalls)
        assertFalse(viewModel.state.value.isSubmitting)
    }

    @Test
    fun emailAlreadyInUse_isReportedAsAnEffect() = runTest {
        authRepository.failure = BackendError.EmailAlreadyInUse
        val viewModel = createViewModel()
        viewModel.fillIn()

        viewModel.effects.test {
            viewModel.onIntent(SignUpIntent.Submit)
            assertEquals(SignUpEffect.ShowError(BackendError.EmailAlreadyInUse), awaitItem())
        }
    }

    @Test
    fun logIn_navigatesBack() = runTest {
        val viewModel = createViewModel()

        viewModel.effects.test {
            viewModel.onIntent(SignUpIntent.LogInClicked)
            assertEquals(SignUpEffect.NavigateBack, awaitItem())
        }
    }
}
