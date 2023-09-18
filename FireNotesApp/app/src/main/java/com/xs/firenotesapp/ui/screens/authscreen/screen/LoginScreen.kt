@file:OptIn(ExperimentalMaterial3Api::class)

package com.xs.firenotesapp.ui.screens.authscreen.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.xs.firenotesapp.R
import com.xs.firenotesapp.ui.components.MySnackBar
import com.xs.firenotesapp.ui.components.MyTopAppBar
import com.xs.firenotesapp.ui.components.StyledTextField
import com.xs.firenotesapp.ui.components.mycircular
import com.xs.firenotesapp.ui.screens.authscreen.AuthScreenEvent
import com.xs.firenotesapp.ui.screens.authscreen.viewmodel.AuthScreenViewModel
import com.xs.firenotesapp.utils.FirebaseResults
import com.xs.firenotesapp.utils.Routes
import com.xs.firenotesapp.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthScreenViewModel = hiltViewModel(),

    ) {
    val loginFlow = viewModel.loginFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is UiEvent.Navigate -> {
                    navController.navigate(
                        it.route,
                        navOptions = NavOptions.Builder().setPopUpTo(it.route, false).build(),
                        navigatorExtras = null
                    )
                }

                UiEvent.PopBackStack -> {
                    navController.popBackStack()
                }

                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        it.message,
                        it.action,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    })

    Scaffold(modifier = Modifier,
        topBar = {
            MyTopAppBar(
                "Login",
                Icons.Default.AccountCircle
            )

        },
        snackbarHost = {
            MySnackBar(snackBarHostState = snackBarHostState) {

            }
        }) {
        Image(
            painter = painterResource(id = R.drawable.peakpx),
            contentDescription = "Bg",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Card(
                Modifier
                    .padding(10.dp)
                    .align(Alignment.CenterHorizontally)
                    .heightIn(min = 250.dp, max = 350.dp),
                elevation = CardDefaults.elevatedCardElevation(10.dp),
                colors = CardDefaults.cardColors(colorResource(id = R.color.white)),

                ) {
                Column(
                    Modifier.paint(
                        painterResource(id = R.drawable.peakpx),
                        contentScale = ContentScale.Crop,
                        sizeToIntrinsics = false,
                        alignment = Alignment.Center,
                        alpha = 0.5f
                    ),
                    Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Login", color = colorResource(id = R.color.blue),
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Welcome Back!", style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(id = R.color.blueInk)
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    StyledTextField(
                        value = viewModel.emailString,
                        placeholder = "Email",
                        onValueChange = {value->
                            viewModel.onViewModelEvent(AuthScreenEvent.OnChangeEmail(value))
                        },
                        textStyle = MaterialTheme.typography.labelLarge,
                        leadingIcon = Icons.Default.Email,
                    )

                    Spacer(modifier = Modifier.padding(5.dp))
                    StyledTextField(
                        value = viewModel.passwordString,
                        onValueChange = {value->
                            viewModel.onViewModelEvent(AuthScreenEvent.OnChangePassword(value))
                        },
                        placeholder = "Password",
                        textStyle = MaterialTheme.typography.labelLarge,
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (viewModel.isPasswordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24,
                        visibility = viewModel.isPasswordVisible,
                        onVisible = {
                            viewModel.onViewModelEvent(AuthScreenEvent.OnVisibilityChangePassword)
                        }
                    )
                    ElevatedButton(
                        modifier = Modifier
                            .padding(5.dp),
                        onClick = {
                            viewModel.onViewModelEvent(AuthScreenEvent.OnLoginAttempt)
                        },
                    ) {
                        Text(text = "Login", style = MaterialTheme.typography.labelMedium)
                    }
                    TextButton(onClick = { viewModel.onViewModelEvent(AuthScreenEvent.OnCreateAccountClicked) }) {
                        Text(text = stringResource(R.string.don_t_have_an_account_click_here))
                    }
                }
            }
        }

        //This is for the when state changes
        loginFlow.value?.let { event ->
            when (event) {
                is FirebaseResults.Failure -> {
                    viewModel.sendUiEvent(UiEvent.ShowSnackBar("Login: ${event.exception.message}"))
                }

                FirebaseResults.Loading -> {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .background(colorResource(id = R.color.white)),
                        Arrangement.Center,
                        Alignment.CenterHorizontally,
                    ) {
                        mycircular()
                        Text(text = "Logging in\nPlease Wait")
                    }
                }

                is FirebaseResults.Success -> {
                    viewModel.sendUiEvent(UiEvent.Navigate(Routes.HomeScreen))
                }
            }
        }
    }
}

