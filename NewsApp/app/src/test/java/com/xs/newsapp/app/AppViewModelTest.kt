package com.xs.newsapp.app

import com.xs.newsapp.data.model.Session
import com.xs.newsapp.testing.MainDispatcherRule
import com.xs.newsapp.testing.TestAuthRepository
import com.xs.newsapp.testing.ayesha
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/** The app shell decides which flow is shown purely from its state: splash, logged out or logged in. */
class AppViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val authRepository = TestAuthRepository(initialSession = Session.Unknown)

    private fun createViewModel() = AppViewModel(authRepository)

    @Test
    fun splash_staysUntilItsAnimationHasFinished() = runTest {
        authRepository.setSession(Session.SignedIn(ayesha))
        val viewModel = createViewModel()
        assertEquals(AppDestination.Splash, viewModel.state.value.destination)

        viewModel.onIntent(AppIntent.SplashFinished)

        assertEquals(AppDestination.Main, viewModel.state.value.destination)
    }

    @Test
    fun splash_staysUntilTheSessionIsKnown() = runTest {
        val viewModel = createViewModel()
        viewModel.onIntent(AppIntent.SplashFinished)
        assertEquals(AppDestination.Splash, viewModel.state.value.destination)

        authRepository.setSession(Session.SignedOut)

        assertEquals(AppDestination.Auth, viewModel.state.value.destination)
    }

    @Test
    fun signingInAndOut_switchesBetweenTheFlows() = runTest {
        authRepository.setSession(Session.SignedOut)
        val viewModel = createViewModel()
        viewModel.onIntent(AppIntent.SplashFinished)
        assertEquals(AppDestination.Auth, viewModel.state.value.destination)

        authRepository.setSession(Session.SignedIn(ayesha))
        assertEquals(AppDestination.Main, viewModel.state.value.destination)

        authRepository.setSession(Session.SignedOut)
        assertEquals(AppDestination.Auth, viewModel.state.value.destination)
    }
}
