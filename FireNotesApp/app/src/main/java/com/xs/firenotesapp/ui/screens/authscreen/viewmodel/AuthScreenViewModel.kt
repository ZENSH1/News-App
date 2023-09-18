package com.xs.firenotesapp.ui.screens.authscreen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.xs.firenotesapp.data.auth.AuthRepository
import com.xs.firenotesapp.data.users.UsersRepository
import com.xs.firenotesapp.ui.screens.authscreen.AuthScreenEvent
import com.xs.firenotesapp.utils.FirebaseResults
import com.xs.firenotesapp.utils.Routes
import com.xs.firenotesapp.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<FirebaseResults<FirebaseUser>?>(null)
    val loginFlow: StateFlow<FirebaseResults<FirebaseUser>?> = _loginFlow

    private val _signUpFlow = MutableStateFlow<FirebaseResults<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<FirebaseResults<FirebaseUser>?> = _signUpFlow

    val currentUser
        get() = authRepository.currentUser


    init {
        viewModelScope.launch {
            if (authRepository.currentUser != null) {
                _loginFlow.value = FirebaseResults.Success(authRepository.currentUser!!)
            }
        }
    }


    //Variable Stuff
    var isPasswordVisible by mutableStateOf(false)
        private set
    var isConfirmPasswordVisible by mutableStateOf(false)
        private set
    var emailString by mutableStateOf("")
        private set
    var passwordString by mutableStateOf("")
        private set
    var confirmPasswordString by mutableStateOf("")
        private set
    var username by mutableStateOf("")
        private set

    fun onViewModelEvent(event: AuthScreenEvent) {
        when (event) {
            is AuthScreenEvent.OnChangeConfirmPassword -> {
                confirmPasswordString = event.value
            }

            is AuthScreenEvent.OnChangeEmail -> {
                emailString = event.value
            }

            is AuthScreenEvent.OnChangePassword -> {
                passwordString = event.value
            }

            is AuthScreenEvent.OnChangeUsername -> {
                username = event.value
            }

            AuthScreenEvent.OnCreateAccountClicked -> {
                sendUiEvent(UiEvent.Navigate(Routes.AuthNav.SignUpScreen))
            }

            AuthScreenEvent.OnLoginAttempt -> {
                viewModelScope.launch {
                    _loginFlow.value = FirebaseResults.Loading
                    val result = authRepository.login(emailString, passwordString)
                    _loginFlow.value = result
                    if (authRepository.currentUser!= null){
                        sendUiEvent(UiEvent.Navigate(Routes.HomeScreen))
                    }
                }
            }

            AuthScreenEvent.OnVisibilityChangeConfirmPassword -> {
                isConfirmPasswordVisible = !isConfirmPasswordVisible
            }

            AuthScreenEvent.OnVisibilityChangePassword -> {
                isPasswordVisible = !isPasswordVisible
            }

            AuthScreenEvent.OnSignUpAttempt -> {
                viewModelScope.launch {
                    _signUpFlow.value = FirebaseResults.Loading
                    val result = authRepository.signUp(username,emailString, passwordString)
                    _signUpFlow.value = result
                    if (authRepository.currentUser != null){
                        sendUiEvent(UiEvent.Navigate(Routes.HomeScreen))
                    }
                }
            }
        }
    }

    private var _uiEvent = Channel<UiEvent>()
    var uiEvent = _uiEvent.receiveAsFlow()

     fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}