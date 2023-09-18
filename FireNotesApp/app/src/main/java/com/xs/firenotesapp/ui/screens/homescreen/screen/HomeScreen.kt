@file:OptIn(ExperimentalMaterial3Api::class)

package com.xs.firenotesapp.ui.screens.homescreen.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.xs.firenotesapp.ui.components.MyBottomBar
import com.xs.firenotesapp.ui.components.MySnackBar
import com.xs.firenotesapp.ui.components.MyTopAppBar
import com.xs.firenotesapp.ui.screens.homescreen.nav.HomeNav
import com.xs.firenotesapp.ui.screens.homescreen.viewmodel.HomeScreenViewModel
import com.xs.firenotesapp.utils.Routes
import com.xs.firenotesapp.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterial3Api
@Composable
fun HomeScreen(
    navController:NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val homeNavController = rememberNavController()
    var selected by remember { mutableIntStateOf(0) }

    //For Handling Ui Events
    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        it.message,
                        it.action,
                        duration = SnackbarDuration.Short
                    )
                }

                is UiEvent.Navigate -> {
                   homeNavController.navigate(it.route)
                }
                is UiEvent.PopBackStack -> {
                    homeNavController.popBackStack()
                }


            }
        }
    })

    Scaffold(
        snackbarHost = {
            MySnackBar(snackBarHostState = snackBarHostState) { }
        },
        bottomBar = {
            MyBottomBar(
                onViewClicked = {
                viewModel.sendUiEvent(UiEvent.Navigate(Routes.HomeNav.ViewNewsScreen))
                selected = 0
            },
                onPostClicked = {  viewModel.sendUiEvent(UiEvent.Navigate(Routes.HomeNav.PostNewsScreen))
                selected = 1 },
                onProfileClicked = {
                  viewModel.sendUiEvent(UiEvent.Navigate(Routes.HomeNav.MyProfileScreen))
                  selected = 3
                },
                selected = selected)
        },
        topBar = { MyTopAppBar(title = "HomePage", icon = Icons.Default.Home) }
    ) {
        Column(Modifier.padding(it).fillMaxSize()) {
            HomeNav(mainNavHostController = navController, navHostController = homeNavController)
        }
    }
}

