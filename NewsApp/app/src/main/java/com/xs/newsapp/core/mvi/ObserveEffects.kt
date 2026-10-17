package com.xs.newsapp.core.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Collects a store's one-off [effects] while the screen is at least STARTED.
 *
 * `Dispatchers.Main.immediate` hands each effect over in the same frame it is received, so an
 * effect cannot be lost between "received from the channel" and "handled" when the screen stops.
 */
@Composable
fun <E> ObserveEffects(effects: Flow<E>, onEffect: (E) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnEffect by rememberUpdatedState(onEffect)
    LaunchedEffect(effects, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                effects.collect { effect -> currentOnEffect(effect) }
            }
        }
    }
}
