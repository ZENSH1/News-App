package com.xs.firenotesapp.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.xs.firenotesapp.ui.screens.homescreen.screen.HomeScreen
import com.xs.firenotesapp.ui.screens.splashscreen.SplashScreen
import com.xs.firenotesapp.utils.Routes

@ExperimentalMaterial3Api
@Composable
fun mainNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SplashScreen) {
        composable(Routes.SplashScreen) { SplashScreen(onNavigate = {
                    navController.navigate(
                        it.route,
                        navOptions = NavOptions.Builder().setPopUpTo(Routes.SplashScreen, true)
                            .build(),
                        null
                    )
                },) }
        AuthScreenNav(navController)
        composable(Routes.HomeScreen){
            HomeScreen(navController)
        }
        profileNav(navController)
    }
}


