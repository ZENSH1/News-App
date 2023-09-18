package com.xs.firenotesapp.ui.screens.homescreen.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.xs.firenotesapp.ui.screens.homescreen.screen.MyProfileScreen
import com.xs.firenotesapp.ui.screens.homescreen.screen.PostNewsScreen
import com.xs.firenotesapp.ui.screens.homescreen.screen.ViewNewsScreen
import com.xs.firenotesapp.utils.Routes


@Composable
fun HomeNav(
    mainNavHostController: NavHostController
    ,navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = Routes.HomeNav.ViewNewsScreen){
        composable(Routes.HomeNav.ViewNewsScreen){
            ViewNewsScreen(navController = mainNavHostController)
        }
        composable(Routes.HomeNav.PostNewsScreen){
            PostNewsScreen()
        }
        composable(Routes.HomeNav.MyProfileScreen){
            MyProfileScreen (navController = mainNavHostController)
        }
    }
}