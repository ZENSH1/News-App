package com.xs.newsapp.core.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

/**
 * The MVI "store" behind a screen. Every screen in the app follows the same loop:
 *
 * ```
 *            ┌──────────────────────────── State ◀──────────────────────────┐
 *            ▼                                                              │
 *          View ──Intent──▶ onIntent() ──▶ handleIntent() ──Mutation──▶ Reducer (pure)
 *            ▲                                   │
 *            └───────────── Effect (one-off) ◀───┘
 * ```
 *
 * - **State** ([S]): one immutable object describing everything the screen shows.
 * - **Intent** ([I]): something the user wants ("submit", "email changed"). The only input.
 * - **Mutation** ([M]): a fact produced while handling an intent ("submission started").
 * - **Reducer**: the pure function `(State, Mutation) -> State`. The *only* place state changes.
 * - **Effect** ([E]): a one-off event that must not be replayed on rotation (navigate, snackbar).
 *
 * It is a Jetpack [ViewModel], so it survives configuration changes and has a lifecycle-aware
 * `viewModelScope` for async work.
 */
abstract class MviViewModel<S : Any, I : Any, M : Any, E : Any>(
    initialState: S,
    private val reducer: Reducer<S, M>,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)

    /** The single source of truth for the screen. The View renders this and nothing else. */
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = Channel<E>(Channel.BUFFERED)

    /** One-off events. Each one is delivered to exactly one collector, even if it subscribes late. */
    val effects: Flow<E> = _effects.receiveAsFlow()

    /** The latest state, for decisions inside [handleIntent]. */
    protected val currentState: S get() = _state.value

    /** The only entry point for the View. */
    fun onIntent(intent: I) = handleIntent(intent)

    /**
     * Interprets an intent: validate, [mutate] the state, launch async work in `viewModelScope`,
     * and [sendEffect] one-off events. This is where side effects live, never in the reducer.
     */
    protected abstract fun handleIntent(intent: I)

    /** Applies [mutation] through the reducer. Atomic, so safe to call from any thread. */
    protected fun mutate(mutation: M) {
        _state.update { state -> reducer.reduce(state, mutation) }
    }

    protected fun sendEffect(effect: E) {
        _effects.trySend(effect)
    }
}

/**
 * The heart of MVI: given the current state and what happened, return the next state.
 * Pure (no I/O, no clocks, no randomness), so every screen's logic can be unit tested in isolation.
 */
fun interface Reducer<S, M> {
    fun reduce(state: S, mutation: M): S
}
