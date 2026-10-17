package com.xs.newsapp.navigation

import androidx.navigation3.runtime.NavKey

/** The only thing that changes [NavigationState]. */
class Navigator(val state: NavigationState) {

    fun navigate(route: NavKey) {
        if (route in state.backStacks.keys) {
            if (route == state.topLevelRoute) {
                // Reselecting a tab pops it to its root.
                state.backStacks.getValue(route).popToRoot()
            } else {
                state.topLevelRoute = route
            }
        } else {
            val stack = state.backStacks.getValue(state.topLevelRoute)
            // Ignore double taps.
            if (stack.lastOrNull() != route) stack.add(route)
        }
    }

    fun goBack() {
        val currentStack = state.backStacks.getValue(state.topLevelRoute)
        if (currentStack.last() == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            currentStack.removeAt(currentStack.lastIndex)
        }
    }

    private fun MutableList<NavKey>.popToRoot() {
        while (size > 1) removeAt(lastIndex)
    }
}
