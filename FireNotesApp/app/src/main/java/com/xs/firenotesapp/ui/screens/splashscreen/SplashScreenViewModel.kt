package com.xs.firenotesapp.ui.screens.splashscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xs.firenotesapp.utils.Routes
import com.xs.firenotesapp.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
): ViewModel() {

    var logoMovement by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            delay(1000)//Waiting for Screen to start
            logoMovement = true //Start Animation
            delay(2200)
            sendUiEvent(UiEvent.Navigate(Routes.AuthScreen)) //Splash Screen Ends
        }
    }
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}