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

// Navigation 3 keys; @Serializable so back stacks survive process death.

@Serializable data object LoginRoute : NavKey

@Serializable data object SignUpRoute : NavKey

@Serializable data object FeedRoute : NavKey

@Serializable data object PublishRoute : NavKey

@Serializable data object ProfileRoute : NavKey

@Serializable data object EditProfileRoute : NavKey

/** Bottom bar tabs. */
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
