package com.xs.firenotesapp.ui.screens.profilescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.xs.firenotesapp.data.auth.AuthRepository
import com.xs.firenotesapp.data.users.UsersRepository
import com.xs.firenotesapp.utils.FirebaseResults
import com.xs.firenotesapp.utils.Routes
import com.xs.firenotesapp.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UsersRepository,
):ViewModel() {
    private val currentUser: FirebaseUser? = authRepository.currentUser

    private lateinit var userData: Flow<FirebaseResults<DocumentSnapshot>>



    init {
        if (authRepository.currentUser == null){
            sendUiEvent(UiEvent.Navigate(Routes.AuthScreen))
        }else{
            userData = userRepository.getUser(currentUser!!.uid)
        }
    }



    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}