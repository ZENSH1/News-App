package com.xs.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer

/**
 * Creates the logged-in navigation state: one back stack per tab plus the selected tab.
 * Everything is saved, so it survives configuration changes and process death.
 *
 * Adapted from the Navigation 3 "multiple back stacks" recipe (android/skills `navigation-3`).
 */
@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>,
): NavigationState {
    val topLevelRoute = rememberSerializable(
        startRoute,
        topLevelRoutes,
        serializer = MutableStateSerializer(NavKeySerializer()),
    ) {
        mutableStateOf(startRoute)
    }
    val backStacks = topLevelRoutes.associateWith { route -> rememberNavBackStack(route) }
    return remember(startRoute, topLevelRoutes) {
        NavigationState(startRoute = startRoute, topLevelRoute = topLevelRoute, backStacks = backStacks)
    }
}

/**
 * State holder for the tabbed navigation. [Navigator] changes it; `NavDisplay` renders it.
 * (Unidirectional data flow applies to navigation too.)
 */
class NavigationState(
    val startRoute: NavKey,
    topLevelRoute: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>,
) {
    /** The selected tab. */
    var topLevelRoute: NavKey by topLevelRoute

    /**
     * The entries to display: the start tab's stack, plus the selected tab's stack on top of it
     * ("exit through home": Back from another tab returns to the start tab before leaving the app).
     *
     * Every entry is decorated with a saveable-state holder and its own ViewModelStore, so each
     * screen gets its own store (MVI ViewModel), cleared when the screen is popped.
     */
    @Composable
    fun toDecoratedEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): List<NavEntry<NavKey>> {
        val decoratedEntries = backStacks.mapValues { (_, stack) ->
            val decorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
                rememberViewModelStoreNavEntryDecorator<NavKey>(),
            )
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = decorators,
                entryProvider = entryProvider,
            )
        }
        val stacksInUse = if (topLevelRoute == startRoute) listOf(startRoute) else listOf(startRoute, topLevelRoute)
        return stacksInUse.flatMap { decoratedEntries[it].orEmpty() }
    }
}
