package com.xs.firenotesapp.ui.screens.profilescreen

sealed class ProfileScreenEvents {

    data class OnNameChange(val value:String): ProfileScreenEvents()
    data class OnAgeChange(val value:String): ProfileScreenEvents()
    data class OnPasswordChange(val value:String): ProfileScreenEvents()
    data class OnConfirmPasswordChange(val value: String): ProfileScreenEvents()

    object OnChangePassword: ProfileScreenEvents()
    object OnChangeUserData: ProfileScreenEvents()

    object OnChangeProfileImage: ProfileScreenEvents()

}