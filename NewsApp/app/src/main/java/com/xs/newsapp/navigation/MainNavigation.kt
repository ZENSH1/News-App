package com.xs.newsapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.xs.newsapp.feature.editprofile.EditProfileScreen
import com.xs.newsapp.feature.feed.FeedScreen
import com.xs.newsapp.feature.profile.ProfileScreen
import com.xs.newsapp.feature.publish.PublishScreen

/** The logged-in flow: a bottom navigation bar with one back stack per tab. */
@Composable
fun MainNavigation(modifier: Modifier = Modifier) {
    val navigationState = rememberNavigationState(startRoute = FeedRoute, topLevelRoutes = TopLevelRoutes)
    val navigator = remember(navigationState) { Navigator(navigationState) }

    val entryProvider = entryProvider<NavKey> {
        entry<FeedRoute> { FeedScreen() }
        entry<PublishRoute> { PublishScreen() }
        entry<ProfileRoute> { ProfileScreen(onEditProfile = { navigator.navigate(EditProfileRoute) }) }
        entry<EditProfileRoute> { EditProfileScreen(onNavigateBack = navigator::goBack) }
    }

    MainScaffold(
        selectedTab = navigationState.topLevelRoute,
        onTabSelected = navigator::navigate,
        modifier = modifier,
    ) {
        NavDisplay(
            entries = navigationState.toDecoratedEntries(entryProvider),
            onBack = navigator::goBack,
        )
    }
}

/** The logged-in frame: the current screen above the bottom navigation bar. Stateless, so it can be previewed. */
@Composable
fun MainScaffold(
    selectedTab: NavKey,
    onTabSelected: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        // No insets here: every screen handles its own, and the bar pads itself for the navigation bar.
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    val selected = destination.route == selectedTab
                    NavigationBarItem(
                        selected = selected,
                        onClick = { onTabSelected(destination.route) },
                        icon = {
                            Icon(
                                imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                contentDescription = null,
                            )
                        },
                        label = { Text(stringResource(destination.label)) },
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                // The bar already covers the navigation bar inset, so screens must not add it again.
                .consumeWindowInsets(innerPadding),
        ) {
            content()
        }
    }
}
