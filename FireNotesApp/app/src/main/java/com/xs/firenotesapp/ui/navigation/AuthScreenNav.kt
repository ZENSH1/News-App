package com.xs.firenotesapp.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.xs.firenotesapp.ui.screens.authscreen.screen.LoginScreen
import com.xs.firenotesapp.ui.screens.authscreen.screen.SignUpScreen
import com.xs.firenotesapp.utils.Routes

fun NavGraphBuilder.AuthScreenNav(navController: NavHostController) {
    navigation(startDestination = Routes.AuthNav.LoginScreen, route= Routes.AuthScreen){
        composable(Routes.AuthNav.LoginScreen){ LoginScreen(navController) }
        composable(Routes.AuthNav.SignUpScreen){ SignUpScreen(navController) }
    }
}