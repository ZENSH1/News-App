package com.xs.newsapp.app

import androidx.lifecycle.viewModelScope
import com.xs.newsapp.core.mvi.MviViewModel
import com.xs.newsapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Store of the app shell. It has no effects: every change it makes is visible in [AppState]. */
@HiltViewModel
class AppViewModel @Inject constructor(
    authRepository: AuthRepository,
) : MviViewModel<AppState, AppIntent, AppMutation, Nothing>(AppState(), AppReducer) {

    init {
        // Not every mutation comes from an intent: data streams feed the reducer too.
        viewModelScope.launch {
            authRepository.session.collect { session -> mutate(AppMutation.SessionChanged(session)) }
        }
    }

    override fun handleIntent(intent: AppIntent) {
        when (intent) {
            AppIntent.SplashFinished -> mutate(AppMutation.SplashFinished)
        }
    }
}
