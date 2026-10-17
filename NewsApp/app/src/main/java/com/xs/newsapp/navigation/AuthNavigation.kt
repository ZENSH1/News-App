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

/**
 * The logged-out flow: Login, with Sign up pushed on top.
 *
 * There is no "go to the feed after login" here. Signing in changes the session, and the app shell
 * swaps this whole flow for [MainNavigation]. The login screen doesn't need to know what comes next.
 */
@Composable
fun AuthNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(LoginRoute)
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            // Each screen gets its own store, cleared when the screen is popped.
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
