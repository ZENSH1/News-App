package com.xs.newsapp.navigation

import androidx.navigation3.runtime.NavKey

/**
 * Handles navigation events by updating [NavigationState]. Screens never touch back stacks
 * directly; they call these methods (usually in response to an MVI effect).
 */
class Navigator(val state: NavigationState) {

    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            if (route == state.topLevelRoute) {
                // Re-selecting the current tab goes back to its first screen.
                state.backStacks.getValue(route).popToRoot()
            } else {
                state.topLevelRoute = route
            }
        } else {
            val stack = state.backStacks.getValue(state.topLevelRoute)
            // Ignore double taps that would push the same screen twice.
            if (stack.lastOrNull() != route) stack.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks.getValue(state.topLevelRoute)
        if (currentStack.last() == state.topLevelRoute) {
            // At the root of a tab: go back to the start tab.
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeAt(currentStack.lastIndex)
        }
    }

    private fun MutableList<NavKey>.popToRoot() {
        while (size > 1) removeAt(lastIndex)
    }
}
