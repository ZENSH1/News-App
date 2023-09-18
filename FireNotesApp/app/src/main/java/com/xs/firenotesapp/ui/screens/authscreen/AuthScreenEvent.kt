package com.xs.firenotesapp.ui.screens.authscreen

sealed class AuthScreenEvent {
    data class OnChangeUsername(val value:String): AuthScreenEvent()
    data class OnChangeEmail(val value:String): AuthScreenEvent()
    data class OnChangePassword(val value:String): AuthScreenEvent()
    data class OnChangeConfirmPassword(val value:String): AuthScreenEvent()
    object OnVisibilityChangePassword: AuthScreenEvent()
    object OnVisibilityChangeConfirmPassword: AuthScreenEvent()
    object OnLoginAttempt: AuthScreenEvent()
    object OnSignUpAttempt: AuthScreenEvent()
    object OnCreateAccountClicked: AuthScreenEvent()
}