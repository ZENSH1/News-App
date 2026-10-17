package com.xs.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.xs.newsapp.feature.login.LoginScreen
import com.xs.newsapp.feature.signup.SignUpScreen

/** Logged-out flow. After sign-in the app shell swaps it for [MainNavigation]. */
@Composable
fun AuthNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(LoginRoute)
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            // A ViewModel store per screen, cleared on pop.
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<LoginRoute> {
                LoginScreen(
                    onNavigateToSignUp = {
                        if (backStack.lastOrNull() != SignUpRoute) backStack.add(SignUpRoute)
                    },
                )
            }
            entry<SignUpRoute> {
                SignUpScreen(
                    onNavigateBack = {
                        if (backStack.lastOrNull() == SignUpRoute) backStack.removeLastOrNull()
                    },
                )
            }
        },
    )
}
