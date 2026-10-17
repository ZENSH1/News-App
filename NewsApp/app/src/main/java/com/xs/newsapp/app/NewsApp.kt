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

/** Root UI: shows the flow for [AppState.destination]. Logging in or out just changes the session. */
@Composable
fun NewsApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Crossfade(targetState = state.destination, modifier = modifier, label = "app destination") { destination ->
        when (destination) {
            AppDestination.Splash -> SplashScreen(onFinished = { viewModel.onIntent(AppIntent.SplashFinished) })
            // Leaving a flow discards its back stacks and screen stores.
            AppDestination.Auth -> AuthNavigation()
            AppDestination.Main -> MainNavigation()
        }
    }
}
