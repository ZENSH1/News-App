package com.xs.newsapp.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.xs.newsapp.R
import kotlinx.serialization.Serializable

// Navigation 3 keys. @Serializable lets the back stacks survive configuration changes and process death.

// Logged-out flow
@Serializable data object LoginRoute : NavKey

@Serializable data object SignUpRoute : NavKey

// Logged-in flow: three tabs, each with its own back stack…
@Serializable data object FeedRoute : NavKey

@Serializable data object PublishRoute : NavKey

@Serializable data object ProfileRoute : NavKey

// …and a screen pushed on top of the Profile tab.
@Serializable data object EditProfileRoute : NavKey

/** The tabs of the bottom navigation bar. The first one is where the user exits the app from. */
enum class TopLevelDestination(
    val route: NavKey,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @param:StringRes val label: Int,
) {
    Feed(FeedRoute, Icons.Filled.Home, Icons.Outlined.Home, R.string.tab_feed),
    Publish(PublishRoute, Icons.Filled.Create, Icons.Outlined.Create, R.string.tab_publish),
    Profile(ProfileRoute, Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle, R.string.tab_profile),
}

val TopLevelRoutes: Set<NavKey> = TopLevelDestination.entries.map { it.route }.toSet()
