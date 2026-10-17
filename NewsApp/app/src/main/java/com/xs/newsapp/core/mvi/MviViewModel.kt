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
 * The MVI store behind a screen: Intent → [handleIntent] → Mutation → [Reducer] → State,
 * plus one-off Effects (navigation, snackbars).
 */
abstract class MviViewModel<S : Any, I : Any, M : Any, E : Any>(
    initialState: S,
    private val reducer: Reducer<S, M>,
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)

    val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = Channel<E>(Channel.BUFFERED)

    /** Each effect is delivered once. */
    val effects: Flow<E> = _effects.receiveAsFlow()

    protected val currentState: S get() = _state.value

    fun onIntent(intent: I) = handleIntent(intent)

    /** Side effects live here, never in the reducer. */
    protected abstract fun handleIntent(intent: I)

    protected fun mutate(mutation: M) {
        _state.update { state -> reducer.reduce(state, mutation) }
    }

    protected fun sendEffect(effect: E) {
        _effects.trySend(effect)
    }
}

/** Pure: the only place state changes. */
fun interface Reducer<S, M> {
    fun reduce(state: S, mutation: M): S
}
