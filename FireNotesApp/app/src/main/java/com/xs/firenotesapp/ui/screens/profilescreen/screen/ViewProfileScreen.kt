package com.xs.firenotesapp.ui.screens.profilescreen.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.xs.firenotesapp.R
import com.xs.firenotesapp.ui.components.MySnackBar
import com.xs.firenotesapp.ui.components.MyTopAppBar
import com.xs.firenotesapp.ui.screens.profilescreen.viewmodel.ProfileViewModel
import com.xs.firenotesapp.utils.Routes
import com.xs.firenotesapp.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProfileScreen(navController: NavHostController,
                      viewModel: ProfileViewModel = hiltViewModel()) {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collectLatest {
            when(it){
                is UiEvent.Navigate -> {
                    navController.navigate(it.route)
                }
                UiEvent.PopBackStack -> {
                    navController.popBackStack()
                }
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        it.message, it.action, duration = SnackbarDuration.Short
                    )
                }
            }
        }
    })

    Scaffold(
        Modifier.fillMaxSize(),
        topBar = {
            MyTopAppBar(
                "View Profile",
                icon = Icons.Default.Edit
                )
            },
        snackbarHost = {
            MySnackBar(snackBarHostState = snackBarHostState) {

                }
            },

    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            Arrangement.SpaceEvenly,
            Alignment.CenterHorizontally) {
            Button(onClick = { viewModel.sendUiEvent(UiEvent.Navigate(Routes.ProfileNav.EditProfileScreen)) }) {
                Text(text = "Edit")
            }
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription ="profile image" )
            Text(text = "Name: ")
            Text(text = "Email: ")
            Text(text = "Account Type: ")
            Text(text = "Age: ")
            Text(text = "Total Posts: ")

        }
    }
}

@Composable
@Preview(showBackground = true)
fun ViewPreview(){
    Column(
        Modifier
            .padding(5.dp)
            .fillMaxSize(),
        Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription ="profile image" )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            text = "Name: ", color = Color.Blue)
        Text(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            text = "Email: ", color = Color.Blue)
        Text(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            text = "Account Type: ", color = Color.Blue)
        Text(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            text = "Age: ", color = Color.Blue)
        Text(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            text = "Total Posts: ", color = Color.Blue)

    }
}