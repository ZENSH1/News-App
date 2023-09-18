package com.xs.firenotesapp.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.xs.firenotesapp.ui.screens.profilescreen.screen.EditProfileScreen
import com.xs.firenotesapp.ui.screens.profilescreen.screen.ViewProfileScreen
import com.xs.firenotesapp.utils.Routes

fun NavGraphBuilder.profileNav(navController: NavHostController) {
    navigation(startDestination = Routes.ProfileNav.ViewProfileScreen, route= Routes.ProfileScreen){
        composable(Routes.ProfileNav.ViewProfileScreen){ ViewProfileScreen(navController) }
        composable(Routes.ProfileNav.EditProfileScreen){ EditProfileScreen(navController) }
    }
}