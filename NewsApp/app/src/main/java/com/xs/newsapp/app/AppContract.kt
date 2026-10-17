package com.xs.newsapp.app

import com.xs.newsapp.core.mvi.Reducer
import com.xs.newsapp.data.model.Session

/** App shell: which part of the app is shown is a function of state. */
data class AppState(
    val splashFinished: Boolean = false,
    val session: Session = Session.Unknown,
) {
    val destination: AppDestination
        get() = when {
            !splashFinished || session == Session.Unknown -> AppDestination.Splash
            session is Session.SignedIn -> AppDestination.Main
            else -> AppDestination.Auth
        }
}

enum class AppDestination { Splash, Auth, Main }

sealed interface AppIntent {
    data object SplashFinished : AppIntent
}

sealed interface AppMutation {
    data object SplashFinished : AppMutation
    data class SessionChanged(val session: Session) : AppMutation
}

object AppReducer : Reducer<AppState, AppMutation> {
    override fun reduce(state: AppState, mutation: AppMutation): AppState = when (mutation) {
        AppMutation.SplashFinished -> state.copy(splashFinished = true)
        is AppMutation.SessionChanged -> state.copy(session = mutation.session)
    }
}
