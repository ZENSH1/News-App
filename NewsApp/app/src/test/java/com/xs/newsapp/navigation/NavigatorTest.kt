package com.xs.newsapp.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertEquals
import org.junit.Test

class NavigatorTest {

    private val state = NavigationState(
        startRoute = FeedRoute,
        topLevelRoute = mutableStateOf(FeedRoute),
        backStacks = TopLevelRoutes.associateWith { route -> NavBackStack(route) },
    )
    private val navigator = Navigator(state)

    private fun stackOf(tab: NavKey): List<NavKey> = state.backStacks.getValue(tab).toList()

    @Test
    fun startsOnTheStartTab() {
        assertEquals(FeedRoute, state.topLevelRoute)
    }

    @Test
    fun screensArePushedOnTheSelectedTab() {
        navigator.navigate(ProfileRoute)
        navigator.navigate(EditProfileRoute)

        assertEquals(ProfileRoute, state.topLevelRoute)
        assertEquals(listOf(ProfileRoute, EditProfileRoute), stackOf(ProfileRoute))
        assertEquals(listOf(FeedRoute), stackOf(FeedRoute))
    }

    @Test
    fun eachTabKeepsItsBackStack() {
        navigator.navigate(ProfileRoute)
        navigator.navigate(EditProfileRoute)

        navigator.navigate(FeedRoute)
        navigator.navigate(ProfileRoute)

        assertEquals(listOf(ProfileRoute, EditProfileRoute), stackOf(ProfileRoute))
    }

    @Test
    fun pushingTheSameScreenTwice_isIgnored() {
        navigator.navigate(ProfileRoute)
        navigator.navigate(EditProfileRoute)
        navigator.navigate(EditProfileRoute)

        assertEquals(listOf(ProfileRoute, EditProfileRoute), stackOf(ProfileRoute))
    }

    @Test
    fun reselectingTheCurrentTab_returnsToItsFirstScreen() {
        navigator.navigate(ProfileRoute)
        navigator.navigate(EditProfileRoute)

        navigator.navigate(ProfileRoute)

        assertEquals(listOf(ProfileRoute), stackOf(ProfileRoute))
    }

    @Test
    fun back_popsTheTab_thenReturnsToTheStartTab() {
        navigator.navigate(ProfileRoute)
        navigator.navigate(EditProfileRoute)

        navigator.goBack()
        assertEquals(ProfileRoute, state.topLevelRoute)
        assertEquals(listOf(ProfileRoute), stackOf(ProfileRoute))

        navigator.goBack()
        assertEquals(FeedRoute, state.topLevelRoute)
    }
}
