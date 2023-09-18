@file:OptIn(ExperimentalMaterial3Api::class)

package com.xs.firenotesapp.ui.screens.authscreen.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.xs.firenotesapp.R
import com.xs.firenotesapp.ui.components.MySnackBar
import com.xs.firenotesapp.ui.screens.authscreen.AuthScreenEvent
import com.xs.firenotesapp.ui.screens.authscreen.viewmodel.AuthScreenViewModel
import com.xs.firenotesapp.ui.components.StyledTextField
import com.xs.firenotesapp.ui.components.mycircular
import com.xs.firenotesapp.utils.FirebaseResults
import com.xs.firenotesapp.utils.Routes
import com.xs.firenotesapp.utils.UiEvent
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navHostController: NavHostController,
    viewModel: AuthScreenViewModel = hiltViewModel(),
) {
    val signupFlow = viewModel.signUpFlow.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = true, block = {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is UiEvent.Navigate -> {
                    navHostController.navigate(
                        it.route,
                        navOptions = navOptions {
                            popUpTo(it.route)
                        },
                        navigatorExtras = null
                    )
                }

                UiEvent.PopBackStack -> {
                    navHostController.popBackStack()
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
        snackbarHost = {
            MySnackBar(snackBarHostState = snackBarHostState) {

            }
        }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .paint(
                    painter = painterResource(id = R.drawable.peakpx),
                    contentScale = ContentScale.FillBounds
                ),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Card(
                Modifier
                    .padding(1.dp)
                    .align(Alignment.CenterHorizontally),
                elevation = CardDefaults.elevatedCardElevation(10.dp),
                colors = CardDefaults.cardColors(Color.Transparent)

            ) {
                Column(
                    Modifier
                        .padding(15.dp)
                        .background(color = Color.Transparent)
                        .fillMaxWidth(),
                    Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Register",
                        color = colorResource(id = R.color.blue),
                        style = MaterialTheme.typography.titleMedium,

                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Input your information below to create an account",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(id = R.color.blueInk)
                    )
                    Spacer(modifier = Modifier.padding(5.dp))
                    StyledTextField(
                        value = viewModel.username,
                        placeholder = "Name",
                        onValueChange = {
                            viewModel.onViewModelEvent(
                                AuthScreenEvent.OnChangeUsername(
                                    it
                                )
                            )
                        },
                        textStyle = MaterialTheme.typography.labelLarge,
                        leadingIcon = Icons.Default.Face,
                    )

                    Spacer(modifier = Modifier.padding(5.dp))
                    StyledTextField(
                        value = viewModel.emailString,
                        placeholder = "Email",
                        onValueChange = {
                            viewModel.onViewModelEvent(
                                AuthScreenEvent.OnChangeEmail(
                                    it
                                )
                            )
                        },
                        textStyle = MaterialTheme.typography.labelLarge,
                        leadingIcon = Icons.Default.Email,
                    )

                    Spacer(modifier = Modifier.padding(5.dp))
                    StyledTextField(
                        value = viewModel.passwordString,
                        placeholder = "Password",
                        onValueChange = {
                            viewModel.onViewModelEvent(
                                AuthScreenEvent.OnChangePassword(
                                    it
                                )
                            )
                        },
                        textStyle = MaterialTheme.typography.labelLarge,
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (viewModel.isPasswordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24,
                        onVisible = { viewModel.onViewModelEvent(AuthScreenEvent.OnVisibilityChangePassword) },
                        visibility = viewModel.isPasswordVisible

                    )

                    Spacer(modifier = Modifier.padding(5.dp))
                    StyledTextField(
                        value = viewModel.confirmPasswordString,
                        placeholder = "Confirm Password",
                        onValueChange = {
                            viewModel.onViewModelEvent(AuthScreenEvent.OnChangeConfirmPassword(it))
                        },
                        textStyle = MaterialTheme.typography.labelLarge,
                        leadingIcon = Icons.Default.Lock,
                        trailingIcon = if (viewModel.isConfirmPasswordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24,
                        onVisible = {
                            viewModel.onViewModelEvent(AuthScreenEvent.OnVisibilityChangeConfirmPassword)
                        },
                        visibility = viewModel.isConfirmPasswordVisible
                    )

                    ElevatedButton(
                        modifier = Modifier
                            .padding(5.dp),
                        onClick = { viewModel.onViewModelEvent(AuthScreenEvent.OnSignUpAttempt) }) {
                        Text(text = "Sign Up", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }

    signupFlow.value?.let {
        when (it) {
            is FirebaseResults.Failure -> {
                viewModel.sendUiEvent(UiEvent.ShowSnackBar("Error Signup Failed"))
            }

            FirebaseResults.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Transparent),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    mycircular()
                    Text(text = "Signing Up\nPlease Wait")
                }
            }

            is FirebaseResults.Success -> {
                viewModel.sendUiEvent(UiEvent.Navigate(Routes.HomeScreen))
            }
        }
    }
}