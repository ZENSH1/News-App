package com.xs.firenotesapp.utils

sealed class UiEvent {
    data class ShowSnackBar(val message:String,val action:String? = null):UiEvent()
    object PopBackStack: UiEvent()
    data class Navigate(val route:String): UiEvent()
}