package com.xs.newsapp.app

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xs.newsapp.feature.splash.SplashScreen
import com.xs.newsapp.navigation.AuthNavigation
import com.xs.newsapp.navigation.MainNavigation

/**
 * The root of the UI. Like every other screen, it renders one state ([AppState]) and sends intents
 * back. Logging in or out changes the session, the state's [AppState.destination] changes, and the
 * matching flow is shown. No screen ever navigates "to login" or "to home" itself.
 */
@Composable
fun NewsApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Crossfade(targetState = state.destination, modifier = modifier, label = "app destination") { destination ->
        when (destination) {
            AppDestination.Splash -> SplashScreen(onFinished = { viewModel.onIntent(AppIntent.SplashFinished) })
            // Each flow keeps its own back stacks. Leaving a flow (logging out) discards them,
            // together with the stores of its screens.
            AppDestination.Auth -> AuthNavigation()
            AppDestination.Main -> MainNavigation()
        }
    }
}
